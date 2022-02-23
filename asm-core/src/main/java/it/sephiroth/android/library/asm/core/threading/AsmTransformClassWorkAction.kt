package it.sephiroth.android.library.asm.core.threading

import it.sephiroth.android.library.asm.core.AsmClassVisitor
import it.sephiroth.android.library.asm.core.AsmClassWriter
import it.sephiroth.android.library.asm.core.vo.IPluginData
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

@Suppress("MemberVisibilityCanBePrivate")
abstract class AsmTransformClassWorkAction : WorkAction<AsmTransformClassWorkAction.TransformClassParams> {

    protected val logger: Logger = LoggerFactory.getLogger(AsmTransformClassWorkAction::class.java) as Logger
    protected val tagName: String by lazy { "${parameters.getPluginName().get()}:${this::class.java.simpleName}" }

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

    fun getClassVisitor(classWriter: AsmClassWriter, className: String, superName: String, pluginData: IPluginData): AsmClassVisitor {
        val cls = Class.forName(parameters.getClassVisitorClassName().get())
        val constructor = cls.getConstructor(AsmClassWriter::class.java, String::class.java, String::class.java, IPluginData::class.java)
        return constructor.newInstance(classWriter, className, superName, pluginData) as AsmClassVisitor
    }

    private fun transformSingleFile(pluginData: IPluginData, inputFile: File, destFile: File, classLoader: URLClassLoader) {
        if (!isClassAvailableForTransformation(inputFile.absolutePath)) {
            logger.debug("[$tagName] skipping $inputFile")
            copyFile(inputFile, destFile)
        } else {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                FileUtils.touch(destFile)
                inputStream = FileInputStream(inputFile)
                outputStream = FileOutputStream(destFile)

                logger.debug("[$tagName] transform ${inputFile.name} --> $destFile")

                // 1. Build ClassReader object
                val classReader = ClassReader(inputStream)
                val className = classReader.className
                val superName = classReader.superName

                // 2. Construct the ClassWriter class of ClassVisitor
                var classWriter = AsmClassWriter(className, superName, ClassWriter.COMPUTE_FRAMES, classLoader)

                val preClassVisitor: AsmClassVisitor = getClassVisitor(classWriter, className, superName, pluginData)

                //3. Call back the content read by ClassReader to ClassVisitor interface
                classReader.accept(preClassVisitor, ClassReader.EXPAND_FRAMES)

                if (preClassVisitor.enabled) {
                    logger.debug("[$tagName] Proceed with second pass")
                    classWriter = AsmClassWriter(classReader.className, classReader.superName, ClassWriter.COMPUTE_FRAMES, classLoader)
                    preClassVisitor.secondPass(classWriter, classReader)
                }

                //4. Get the complete byte stream through toByteArray method of classWriter object
                outputStream.write(classWriter.toByteArray())

            } catch (e: Exception) {
                logger.warn("[$tagName] failed to transform ${inputFile}: ${e.message}")
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
    protected open fun isClassAvailableForTransformation(className: String): Boolean {
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
        fun getClassVisitorClassName(): Property<String>
        fun getPluginName(): Property<String>
    }
}
