package it.sephiroth.android.library.debuglog.threading

import it.sephiroth.android.library.debuglog.DebugLogPlugin
import it.sephiroth.android.library.debuglog.asm.pre.ASMClassVisitor
import it.sephiroth.android.library.debuglog.asm.ASMClassWriter
import it.sephiroth.android.library.debuglog.asm.post.PostClassVisitor
import it.sephiroth.android.library.debuglog.asm.vo.IPluginData
import org.apache.commons.io.FileUtils
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logger
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URL
import java.net.URLClassLoader

abstract class TransformClassWorkAction : WorkAction<TransformClassWorkAction.TransformClassParams> {
    companion object {
        const val TAG = DebugLogPlugin.TAG
    }

    private val logger: Logger = LoggerFactory.getLogger(TransformClassWorkAction::class.java) as Logger

    override fun execute() {
        val inputFile = parameters.getSrcFile().get().asFile
        val destFile = parameters.getDstFile().get().asFile
        val pluginData = parameters.getPluginData().get()
        val classLoader = URLClassLoader(parameters.getClassPaths().get().toTypedArray())
        transformSingleFile(pluginData, inputFile, destFile, classLoader)
    }

    private fun copyFile(inputFile: File, destFile: File) {
        if (inputFile.isFile) {
            FileUtils.touch(destFile)
            FileUtils.copyFile(inputFile, destFile)
        }
    }

    private fun transformSingleFile(pluginData: IPluginData, inputFile: File, destFile: File, classLoader: URLClassLoader) {
        if (!isClassAvailableForTransformation(inputFile.absolutePath)) {
            logger.info("[$TAG] Skipping $inputFile")
            copyFile(inputFile, destFile)
        } else {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                FileUtils.touch(destFile)
                inputStream = FileInputStream(inputFile)
                outputStream = FileOutputStream(destFile)

                logger.debug("[$TAG] transformSingleFile(${inputFile.name}, $destFile)")

                // 1. Build ClassReader object
                val classReader = ClassReader(inputStream)
                val className = classReader.className
                val superName = classReader.superName

                // 2. Construct the ClassWriter class of ClassVisitor
                var classWriter = ASMClassWriter(className, superName, ClassWriter.COMPUTE_FRAMES, classLoader)

                val preClassVisitor = ASMClassVisitor(classWriter, className, superName, pluginData)

                //3. Call back the content read by ClassReader to ClassVisitor interface
                classReader.accept(preClassVisitor, ClassReader.EXPAND_FRAMES)

                if (preClassVisitor.enabled) {
                    logger.info("[$TAG] Annotations found in $className. Proceed with transformation.")
                    classWriter = ASMClassWriter(classReader.className, classReader.superName, ClassWriter.COMPUTE_FRAMES, classLoader)
                    classReader.accept(PostClassVisitor(
                        classWriter,
                        className,
                        preClassVisitor.methodsParametersMap
                    ), ClassReader.EXPAND_FRAMES)
                }

                //4. Get the complete byte stream through toByteArray method of classWriter object
                outputStream.write(classWriter.toByteArray())

            } catch (e: Exception) {
                logger.warn("[$TAG] Failed to transform ${inputFile}: ${e.message}")
                e.printStackTrace()
                copyFile(inputFile, destFile)
            } finally {
                try {
                    inputStream?.close()
                } catch (ignored: Throwable) {
                }

                try {
                    outputStream?.close()
                } catch (ignored: Throwable) {
                }
            }
        }
    }

    /**
     * Returns if the given class (className) can be considered for transformation
     * @param className full qualified class name
     * @return
     */
    private fun isClassAvailableForTransformation(className: String): Boolean {
        return className.endsWith(".class")
                && !className.contains("R\$")
                && !className.contains("R.class")
                && !className.contains("BuildConfig.class")
    }

    interface TransformClassParams : WorkParameters {
        fun getSrcFile(): RegularFileProperty
        fun getDstFile(): RegularFileProperty
        fun getClassPaths(): ListProperty<URL>
        fun getPluginData(): Property<IPluginData>
    }
}
