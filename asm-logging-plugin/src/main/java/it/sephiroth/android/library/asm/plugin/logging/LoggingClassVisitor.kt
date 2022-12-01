package it.sephiroth.android.library.asm.plugin.logging

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.utils.StringUtils
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory


/**
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
class LoggingClassVisitor(
    visitor: ClassVisitor,
    private val classContext: ClassContext,
    private val classWriter: ClassWriter,
) : ClassVisitor(ASM_VERSION, visitor) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val tagName = Constants.makeTag(this)
    private val simpleClassName = StringUtils.getSimpleClassName(classContext.currentClassData.className)
    private val className = classContext.currentClassData.className

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        logger.debug("[$tagName] visitMethod(className=$className, methodName=$name, signature=$signature, exceptions=$exceptions)")
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return LoggingMethodVisitor(className, simpleClassName, name, descriptor, mv)
    }

}
