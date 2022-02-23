package it.sephiroth.android.library.asm.core.threading

import it.sephiroth.android.library.asm.core.AsmClassVisitor
import it.sephiroth.android.library.asm.core.AsmClassWriter
import it.sephiroth.android.library.asm.core.vo.IPluginData
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
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
import java.nio.file.Files
import java.nio.file.attribute.FileTime
import java.util.*
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * @author Alessandro Crugnola on 17.11.21 - 13:46
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class AsmTransformJarWorkAction : WorkAction<TransformJarParams> {
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    protected val tagName: String by lazy { "${parameters.getPluginName().get()}:${this::class.java.simpleName}" }

    fun getClassVisitor(
        classWriter: AsmClassWriter,
        className: String,
        superName: String,
        pluginData: IPluginData
    ): AsmClassVisitor {

        val cls = Class.forName(parameters.getClassVisitorClassName().get())
        val constructor = cls.getConstructor(
            AsmClassWriter::class.java,
            String::class.java,
            String::class.java,
            IPluginData::class.java
        )
        return constructor.newInstance(
            classWriter,
            className,
            superName,
            pluginData
        ) as AsmClassVisitor
    }

    override fun execute() {
        val inputFile = parameters.getSrcFile().get().asFile
        val destFile = parameters.getDstFile().get().asFile
        val pluginData = parameters.getPluginData().get()
        val classLoader = URLClassLoader(parameters.getClassPaths().get().toTypedArray())
        val processJar = parameters.getProcessJar().get()

        if (processJar) {
            try {
                transformSingleFile(pluginData, inputFile, destFile, classLoader)
            } catch (t: Throwable) {
                copyFile(inputFile, destFile)
            }
        } else {
            copyFile(inputFile, destFile)
        }
    }

    private fun copyFile(inputFile: File, destFile: File) {
        if (inputFile.isFile) {
            FileUtils.touch(destFile)
            FileUtils.copyFile(inputFile, destFile)
            logger.debug("[$tagName] copying jar ${inputFile.name} --> $destFile")
        }
    }

    private fun transformSingleFile(
        pluginData: IPluginData,
        inputFile: File,
        destFile: File,
        classLoader: URLClassLoader
    ) {
        logger.debug("[$tagName] inspecting jar ${inputFile.name} --> $destFile)")

        val inputZip = ZipFile(inputFile)
        val outputZip =
            ZipOutputStream(BufferedOutputStream(Files.newOutputStream(destFile.toPath())))
        val inEntries: Enumeration<out ZipEntry?> = inputZip.entries()
        while (inEntries.hasMoreElements()) {
            val entry: ZipEntry = inEntries.nextElement()!!
            val originalFile: InputStream = BufferedInputStream(inputZip.getInputStream(entry))
            val outEntry = ZipEntry(entry.name)
            val newEntryContent: ByteArray =
                if (!isClassAvailableForTransformation(outEntry.name.replace("/", "."))) {
                    IOUtils.toByteArray(originalFile)
                } else {
                    transformSingleClassToByteArray(originalFile, pluginData, classLoader)
                }
            val crc32 = CRC32()
            crc32.update(newEntryContent)
            outEntry.crc = crc32.value
            outEntry.method = ZipEntry.STORED
            outEntry.size = newEntryContent.size.toLong()
            outEntry.compressedSize = newEntryContent.size.toLong()
            outEntry.lastAccessTime = ZERO
            outEntry.lastModifiedTime = ZERO
            outEntry.creationTime = ZERO
            outputZip.putNextEntry(outEntry)
            outputZip.write(newEntryContent)
            outputZip.closeEntry()
        }
        outputZip.flush()
        outputZip.close()
    }

    @Throws(IOException::class)
    open fun transformSingleClassToByteArray(
        inputStream: InputStream?,
        pluginData: IPluginData,
        classLoader: URLClassLoader
    ): ByteArray {

        val classReader = ClassReader(inputStream)
        val className = classReader.className
        val superName = classReader.superName

        logger.debug("[$tagName] transforming jar $className")

        var classWriter = AsmClassWriter(className, superName, ClassWriter.COMPUTE_FRAMES, classLoader)
        val classVisitor = getClassVisitor(classWriter, className, superName, pluginData)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

        if (classVisitor.enabled) {
            classWriter = AsmClassWriter(className, superName, ClassWriter.COMPUTE_FRAMES, classLoader)
            classVisitor.secondPass(classWriter, classReader)
        }

        return classWriter.toByteArray()
    }

    fun isClassAvailableForTransformation(fullQualifiedClassName: String): Boolean {
        return fullQualifiedClassName.endsWith(".class")
                && !fullQualifiedClassName.contains("R$")
                && !fullQualifiedClassName.contains("R.class")
                && !fullQualifiedClassName.contains("BuildConfig.class")
    }

    companion object {
        private val ZERO = FileTime.fromMillis(0)
    }

}

interface TransformJarParams : WorkParameters {
    fun getSrcFile(): RegularFileProperty
    fun getDstFile(): RegularFileProperty
    fun getClassPaths(): ListProperty<URL>
    fun getPluginData(): Property<IPluginData>
    fun getProcessJar(): Property<Boolean>
    fun getClassVisitorClassName(): Property<String>
    fun getPluginName(): Property<String>
}
