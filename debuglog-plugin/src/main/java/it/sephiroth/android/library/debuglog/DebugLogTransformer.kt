//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.debuglog

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import it.sephiroth.android.library.debuglog.asm.vo.PluginData
import it.sephiroth.android.library.debuglog.threading.TransformClassWorkAction
import it.sephiroth.android.library.debuglog.threading.TransformJarWorkAction
import it.sephiroth.android.library.debuglog.utils.ClassLoaderHelper
import it.sephiroth.android.library.debuglog.utils.StringUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.workers.WorkQueue
import java.io.File
import java.net.URL

@Suppress("DEPRECATION")
class DebugLogTransformer(private val project: Project) : Transform() {
    private val logger: Logger = project.logger
    private lateinit var debugLogPluginExtension: DebugLogPluginExtension
    private val pluginScopes: HashSet<QualifiedContent.Scope> = HashSet()

    init {
        pluginScopes.add(QualifiedContent.Scope.PROJECT)
        pluginScopes.add(QualifiedContent.Scope.SUB_PROJECTS)
        pluginScopes.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        logger.debug("[$TAG] DebugLogTransformer added to project $project.name")
    }

    override fun getName(): String {
        return this::class.java.simpleName
    }


    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return pluginScopes
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)

        // save transformation start time
        val startTime = System.currentTimeMillis()

        // retrieve the registered plugin-extension
        debugLogPluginExtension = project.extensions.getByName(Constants.DEBUGLOG_EXTENSION) as DebugLogPluginExtension

        logger.lifecycle(
                "[$TAG] androidDebugLog[" +
                        "enabled=${debugLogPluginExtension.enabled.get()}, " +
                        "logLevel=${debugLogPluginExtension.logLevel.get()}, " +
                        "debugResult=${debugLogPluginExtension.debugResult.get()}, " +
                        "debugArguments=${debugLogPluginExtension.debugArguments.get()}, " +
                        "runVariant=${debugLogPluginExtension.runVariant.get()}" +
                        "]"
        )

        val enabled = isPluginEnabled(transformInvocation, debugLogPluginExtension)

        logger.debug("[$TAG] project variant: ${transformInvocation.context.variantName} (current: ${debugLogPluginExtension.runVariant.get().name}")
        logger.debug("[$TAG] transformInvocation.incremental: ${transformInvocation.isIncremental}, incremental: $isIncremental")
        logger.lifecycle("[$TAG] Transformation enabled: $enabled")

        processInput(transformInvocation, debugLogPluginExtension, enabled)

        // log total execution time
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        logger.lifecycle("[$TAG] DebugLog Transformation SUCCESS in ${totalTime}ms")
    }

    /**
     * Process all files and directories
     * @param transformInvocation
     * @param debugLogPluginExtension
     */
    private fun processInput(transformInvocation: TransformInvocation, debugLogPluginExtension: DebugLogPluginExtension, enabled: Boolean) {
        // 1. delete all transformed classes if the invocation is not incremental.
        if (!transformInvocation.isIncremental) transformInvocation.outputProvider.deleteAll()

        // 2. create the global classloader for all transforming classes
        val classPaths = ClassLoaderHelper.getClassPathImports(transformInvocation.inputs, transformInvocation.referencedInputs, this.project)
        val workQueue = transformInvocation.context.workerExecutor.noIsolation()

        // 3. proceed with the transformation, using the provided context worker queue
        transformInvocation.inputs.forEach { input ->

            // jar inputs
            input.jarInputs.forEach { jarInput ->
                processJarInput(workQueue, transformInvocation, jarInput)
            }

            // directory inputs
            input.directoryInputs.forEach { directory ->
                if (directory.contentTypes.contains(QualifiedContent.DefaultContentType.CLASSES)) {
                    processDirectoryInput(workQueue, transformInvocation, enabled, directory, classPaths)
                }
            }
        }

        // 4. await executor termination
        // executor.await()
        logger.lifecycle("[$TAG] Waiting for WorkQueue to complete....")
        workQueue.await()
    }

    private fun processJarInput(workQueue: WorkQueue, transformInvocation: TransformInvocation, jarInput: JarInput) {
        logger.debug("[$TAG] processJarInput(${jarInput.file.name})")

        val status = jarInput.status
        val dest = transformInvocation.outputProvider.getContentLocation(jarInput.file.absolutePath, jarInput.contentTypes, jarInput.scopes, Format.JAR)

        if (transformInvocation.isIncremental) {
            when (status) {
                Status.ADDED,
                Status.CHANGED ->
                    transformJar(workQueue, jarInput.file, dest)

                Status.REMOVED ->
                    if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                else -> {
                    // empty
                }
            }
        } else {
            // Forgive me!, Some project will store 3rd-party aar for several copies in dex builder folder, unknown issue.
            if (!transformInvocation.isIncremental) {
                cleanDexBuilderFolder(dest)
            }
            transformJar(workQueue, jarInput.file, dest)
        }
    }

    private fun processDirectoryInput(
            workQueue: WorkQueue,
            transformInvocation: TransformInvocation,
            enabled: Boolean,
            directoryInput: DirectoryInput,
            classPaths: List<URL>
    ) {

        logger.debug("[$TAG] processDirectoryInput(${directoryInput.file})")


        val dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

        FileUtils.forceMkdir(dest)

        if (transformInvocation.isIncremental) {
            val srcDirPath = directoryInput.file.absolutePath
            val destDirPath = dest.absolutePath
            val fileStatusMap = directoryInput.changedFiles

            fileStatusMap.entries.forEach { changedFile ->
                val status = changedFile.value
                val inputFile = changedFile.key
                val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
                val destFile = File(destFilePath)

                logger.debug("[$TAG] inputFile: $inputFile, destFile: $destFile")

                when (status) {
                    Status.ADDED,
                    Status.CHANGED ->
                        if (!enabled) {
                            FileUtils.copyFile(inputFile, destFile)
                        } else {
                            FileUtils.touch(destFile)
                            transformSingleFile(workQueue, inputFile, destFile, classPaths)
                        }
                    Status.REMOVED ->
                        if (destFile.exists()) {
                            FileUtils.forceDelete(destFile)
                        }
                    else -> {
                        // empty
                    }
                }
            }
        } else {
            transformDirectory(workQueue, enabled, directoryInput.file, dest, classPaths)
        }
    }

    private fun transformDirectory(
            workQueue: WorkQueue,
            enabled: Boolean,
            inputDir: File,
            outputDir: File,
            classPaths: List<URL>
    ) {

        logger.debug("[$TAG] transformDirectory(enabled=$enabled, $inputDir, $outputDir)")
        if (!enabled) {
            FileUtils.copyDirectory(inputDir, outputDir)
            return
        }

        val inputDirPath = inputDir.absolutePath
        val outputDirPath = outputDir.absolutePath
        if (inputDir.isDirectory) {
            FileUtils.listFilesAndDirs(inputDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE).forEach { file ->
                if (file.isFile) {
                    logger.debug("[$TAG] Checking file $file (is file: ${file.isFile})")
                    val filePath = file.absolutePath
                    val outputFile = File(filePath.replace(inputDirPath, outputDirPath))
                    transformSingleFile(workQueue, file, outputFile, classPaths)
                }
            }
        } else {
            logger.warn("[$TAG] Not a directory: $inputDir")
        }
    }

    private fun transformSingleFile(workQueue: WorkQueue, inputFile: File, destFile: File, classPaths: List<URL>) {
        workQueue.submit(TransformClassWorkAction::class.java) {
            getSrcFile().set(inputFile)
            getDstFile().set(destFile)
            getClassPaths().set(classPaths)
            getPluginData().set(PluginData(debugLogPluginExtension.logLevel.get(), debugLogPluginExtension.debugResult.get(), debugLogPluginExtension.debugArguments.get()))
        }
    }


    private fun transformJar(workQueue: WorkQueue, srcJar: File, destJar: File) {
        workQueue.submit(TransformJarWorkAction::class.java) {
            getSrcFile().set(srcJar)
            getDstFile().set(destJar)
        }

    }

    private fun cleanDexBuilderFolder(dest: File) {
        logger.debug("[$TAG] cleanDexBuilderFolder($dest)")
        try {
            val dexBuilderDir = StringUtils.replaceLastPart(dest.absolutePath, name, "dexBuilder")
            // intermediates/transforms/dexBuilder/debug
            val file = File(dexBuilderDir).parentFile
            if (file.exists() && file.isDirectory) {
                FileUtils.cleanDirectory(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Returns if the plugin should be executed given the current context and build variant
     * @param transformInvocation
     * @param extension
     * @return
     */
    private fun isPluginEnabled(transformInvocation: TransformInvocation, extension: DebugLogPluginExtension): Boolean {
        if (!extension.enabled.get()) return false
        val runVariant = extension.runVariant.get()
        logger.debug("[$TAG] variant = ${transformInvocation.context.variantName} (runVariant=$runVariant)")

        if (transformInvocation.context.variantName.toLowerCase().endsWith("debug")) {
            return runVariant == RunVariant.Debug || runVariant == RunVariant.Always
        } else if (transformInvocation.context.variantName.toLowerCase().endsWith("release")) {
            return runVariant == RunVariant.Release || runVariant == RunVariant.Always
        } else {
            return false
        }
    }

    companion object {
        const val TAG: String = DebugLogPlugin.TAG

    }
}
