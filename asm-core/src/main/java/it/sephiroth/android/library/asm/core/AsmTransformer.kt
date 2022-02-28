//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.core

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import it.sephiroth.android.library.asm.core.threading.AsmTransformClassWorkAction
import it.sephiroth.android.library.asm.core.threading.AsmTransformJarWorkAction
import it.sephiroth.android.library.asm.core.utils.ClassLoaderHelper
import it.sephiroth.android.library.asm.core.utils.StringUtils
import it.sephiroth.android.library.asm.core.vo.IPluginData
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.workers.WorkQueue
import java.io.File
import java.net.URL

@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")
abstract class AsmTransformer<T : AsmCorePluginExtension, R : IPluginData, Q : AsmClassVisitor>(
    private val project: Project,
    private val extensionName: String,
    private val classVisitor: Class<Q>
) : Transform() {

    protected val tagName: String = extensionName
    protected val logger: Logger = project.logger

    private lateinit var pluginExtension: T

    private val pluginScopes: HashSet<QualifiedContent.Scope> = HashSet()

    private val resultData = ResultData()

    init {
        pluginScopes.add(QualifiedContent.Scope.PROJECT)
        pluginScopes.add(QualifiedContent.Scope.SUB_PROJECTS)
        pluginScopes.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        logger.debug("[$tagName] ${this::class.java.simpleName} added to project $project.name")
    }

    abstract fun processJars(pluginExtension: T): Boolean

    private fun getPluginExtension(): T {
        return pluginExtension
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
        @Suppress("UNCHECKED_CAST")
        pluginExtension = project.extensions.getByType(AsmCoreBasePluginExtension::class.java).extensions.getByName(extensionName) as T

        logger.lifecycle("[$tagName] $pluginExtension")

        val enabled = isPluginEnabled(transformInvocation, pluginExtension)

        logger.lifecycle("[$tagName] incremental: ${transformInvocation.isIncremental}, enabled: $enabled")

        if (!enabled) {
            logger.lifecycle("[$tagName] transformation not enabled")
            if (!pluginExtension.enabled) {
                logger.lifecycle("[$tagName] $extensionName disabled")
            } else {
                logger.lifecycle("[$tagName] current variant `${transformInvocation.context.variantName}` not enabled")
            }
        } else {
            logger.lifecycle("[$tagName] transformation enabled: $enabled")
        }

        processInput(transformInvocation, enabled)

        // log total execution time
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        logger.lifecycle("[$tagName] Transformation SUCCESS in ${totalTime}ms")
        logger.lifecycle("[$tagName] Processed ${resultData.transformedFiles} classes")
        logger.lifecycle("[$tagName] Processed ${resultData.transformedJars} jars")
    }

    /**
     * Process all files and directories
     * @param transformInvocation
     */
    private fun processInput(transformInvocation: TransformInvocation, enabled: Boolean) {
        // 1. delete all transformed classes if the invocation is not incremental.
        if (!transformInvocation.isIncremental) transformInvocation.outputProvider.deleteAll()

        // 2. create the global classloader for all transforming classes
        val classPaths = ClassLoaderHelper.getClassPathImports(
            transformInvocation.inputs,
            transformInvocation.referencedInputs,
            this.project
        )
        val workQueue = transformInvocation.context.workerExecutor.noIsolation()

        // 3. proceed with the transformation, using the provided context worker queue
        transformInvocation.inputs.forEach { input ->

            // plugin requested to process also third party jars
            val shouldProcessJars = processJars(getPluginExtension())

            // jar inputs
            input.jarInputs.forEach { jarInput ->
                processJarInput(workQueue, transformInvocation, enabled, shouldProcessJars, jarInput, classPaths)
            }

            // directory inputs
            input.directoryInputs.forEach { directory ->
                if (directory.contentTypes.contains(QualifiedContent.DefaultContentType.CLASSES)) {
                    processDirectoryInput(
                        workQueue,
                        transformInvocation,
                        enabled,
                        directory,
                        classPaths
                    )
                }
            }
        }

        // 4. await executor termination
        // executor.await()
        logger.lifecycle("[$tagName] Waiting for WorkQueue to complete....")
        workQueue.await()
    }

    private fun processJarInput(
        workQueue: WorkQueue,
        transformInvocation: TransformInvocation,
        enabled: Boolean,
        processJarsEnabled: Boolean,
        jarInput: JarInput,
        classPaths: List<URL>
    ) {
        logger.debug("[$tagName] processing jat `${jarInput.file.name}`")

        val status = jarInput.status
        val destFile = transformInvocation.outputProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )

        if (transformInvocation.isIncremental) {
            when (status) {
                Status.ADDED,
                Status.CHANGED ->
                    if (!enabled || !processJarsEnabled) {
                        // just copy the file as it is
                        FileUtils.copyFile(jarInput.file, destFile)
                    } else {
                        FileUtils.touch(destFile)
                        transformJar(workQueue, jarInput.file, destFile, classPaths)
                    }

                Status.REMOVED ->
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
                else -> {
                    // empty
                }
            }
        } else {
            // Forgive me!, Some project will store 3rd-party aar for several copies in dex builder folder, unknown issue.
            if (!transformInvocation.isIncremental) {
                cleanDexBuilderFolder(destFile)
            }

            if (enabled && processJarsEnabled) {
                FileUtils.touch(destFile)
                transformJar(workQueue, jarInput.file, destFile, classPaths)
            } else {
                // just copy the file as it is
                FileUtils.copyFile(jarInput.file, destFile)
            }
        }
    }

    private fun processDirectoryInput(
        workQueue: WorkQueue,
        transformInvocation: TransformInvocation,
        enabled: Boolean,
        directoryInput: DirectoryInput,
        classPaths: List<URL>
    ) {

        logger.debug("[$tagName] processDirectoryInput(${directoryInput.file})")


        val dest = transformInvocation.outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )

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

                logger.debug("[$tagName] inputFile: $inputFile, destFile: $destFile")

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

        logger.debug("[$tagName] transformDirectory(enabled=$enabled, $inputDir, $outputDir)")
        if (!enabled) {
            FileUtils.copyDirectory(inputDir, outputDir)
            return
        }

        val inputDirPath = inputDir.absolutePath
        val outputDirPath = outputDir.absolutePath
        if (inputDir.isDirectory) {
            FileUtils.listFilesAndDirs(inputDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
                .forEach { file ->
                    if (file.isFile) {
                        logger.debug("[$tagName] Checking file $file (is file: ${file.isFile})")
                        val filePath = file.absolutePath
                        val outputFile = File(filePath.replace(inputDirPath, outputDirPath))
                        transformSingleFile(workQueue, file, outputFile, classPaths)
                    }
                }
        } else {
            logger.warn("[$tagName] Not a directory: $inputDir")
        }
    }

    protected fun transformSingleFile(
        workQueue: WorkQueue,
        inputFile: File,
        destFile: File,
        classPaths: List<URL>
    ) {
        val pluginData: R = generatePluginData(getPluginExtension())

        workQueue.submit(AsmTransformClassWorkAction::class.java) {
            getSrcFile().set(inputFile)
            getDstFile().set(destFile)
            getClassPaths().set(classPaths)
            getPluginData().set(pluginData)
            getClassVisitorClassName().set(classVisitor.canonicalName)
            getPluginName().set(extensionName)
        }

        resultData.transformedFiles += 1
    }

    abstract fun generatePluginData(pluginExtension: T): R


    protected fun transformJar(
        workQueue: WorkQueue,
        srcJar: File,
        destJar: File,
        classPaths: List<URL>
    ) {
        val pluginData: R = generatePluginData(getPluginExtension())

        workQueue.submit(AsmTransformJarWorkAction::class.java) {
            getSrcFile().set(srcJar)
            getDstFile().set(destJar)
            getClassPaths().set(classPaths)
            getPluginData().set(pluginData)
            getClassVisitorClassName().set(classVisitor.canonicalName)
            getProcessJar().set(processJars(getPluginExtension()))
            getPluginName().set(extensionName)
        }

        resultData.transformedJars += 1
    }

    private fun cleanDexBuilderFolder(dest: File) {
        logger.debug("[$tagName] cleanDexBuilderFolder($dest)")
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
    protected open fun isPluginEnabled(transformInvocation: TransformInvocation, extension: T): Boolean {
        if (!extension.enabled) return false
        return isPluginEnabledForVariant(extension, transformInvocation.context.variantName)
    }

    /**
     * Returns true if the plugin is enabled for the current selected variant
     */
    protected open fun isPluginEnabledForVariant(extension: T, variantName: String): Boolean {
        val runVariant = extension.runVariant
        return variantName.matches(Regex(runVariant))
    }

    class ResultData {
        var transformedFiles: Int = 0
        var transformedJars: Int = 0
    }
}
