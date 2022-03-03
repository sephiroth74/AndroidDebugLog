package it.sephiroth.android.library.asm.plugin.logginglevel

import it.sephiroth.android.library.asm.plugin.core.AsmClassVisitor
import it.sephiroth.android.library.asm.plugin.core.AsmClassWriter
import it.sephiroth.android.library.asm.plugin.core.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import it.sephiroth.android.library.asm.plugin.logginglevel.vo.LoggingPluginData
import org.objectweb.asm.ClassReader
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


/**
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
class LoggingLevelClassVisitor(
    cv: AsmClassWriter,
    className: String,
    superName: String,
    pluginData: IPluginData
) : AsmClassVisitor(cv, className, superName, pluginData) {

    private val tagName = Constants.makeTag(this)

    override fun visitField(access: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor {
        return if (className == Constants.LoggingLevel.CLASS_NAME && Constants.LoggingLevel.MIN_PRIORITY.matches(name, descriptor)) {
            val data = pluginData as LoggingPluginData
            logger.lifecycle("[$tagName] Setting the const value of ${Constants.LoggingLevel.MIN_PRIORITY} to ${data.minLogLevel.value}")
            super.visitField(access, name, descriptor, signature, data.minLogLevel.value)
        } else {
            super.visitField(access, name, descriptor, signature, value)
        }
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        logger.debug("[$tagName] visitMethod(className=$className, methodName=$name, signature=$signature, exceptions=$exceptions)")
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        val data = pluginData as LoggingPluginData

        // do not touch NullLogger
        if (className == Constants.NullLogger.CLASS_NAME) return mv

        if (className == Constants.LoggingLevel.CLASS_NAME) {
            if (Constants.LoggingLevel.GET_MIN_PRIORITY.matches(name, descriptor)) {
                return GetMinPriorityMethodVisitor(mv, data.minLogLevel.value)
            }
            return mv
        }

        val mv2 = LoggingLevelMethodVisitor(className, simpleClassName, name, descriptor, mv, data, object : LoggingLevelMethodVisitor.Callback {
            override fun accept() {
                requireSecondPass = true
            }
        })
        return mv2
    }

    override fun executeSecondPass(classWriter: AsmClassWriter, classReader: ClassReader) {
        // do nothing
    }

}


/**
 * Replace body of LoggingLevel.getMinPriority method
 */
class GetMinPriorityMethodVisitor(private val target: MethodVisitor, private val minPriority: Int) : MethodVisitor(it.sephiroth.android.library.asm.plugin.core.Constants.ASM_VERSION, null) {
    override fun visitCode() {
        target.visitCode()
        AsmVisitorUtils.visitInt(target, minPriority)
        target.visitInsn(Opcodes.IRETURN)
        target.visitMaxs(-1, -1)
        target.visitEnd()
    }
}
