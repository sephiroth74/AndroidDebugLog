package it.sephiroth.android.library.asm.logginglevel

import it.sephiroth.android.library.asm.core.AsmClassVisitor
import it.sephiroth.android.library.asm.core.AsmClassWriter
import it.sephiroth.android.library.asm.logginglevel.vo.LoggingPluginData
import org.objectweb.asm.ClassReader
import org.objectweb.asm.MethodVisitor


/**
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
class LoggingLevelClassVisitor(
    cv: AsmClassWriter,
    className: String,
    superName: String,
    pluginData: it.sephiroth.android.library.asm.core.vo.IPluginData
) : AsmClassVisitor(cv, className, superName, pluginData) {

    private val tagName = Constants.makeTag(this)

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        logger.debug("[$tagName] visitMethod(className=$className, methodName=$name, signature=$signature, exceptions=$exceptions)")
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        val data = pluginData as LoggingPluginData

        // do not touch NullLogger
        if (className == Constants.NullLogger.CLASS_NAME) return mv

        val mv2 = LoggingLevelMethodVisitor(className, simpleClassName, name, descriptor, mv, data, object : LoggingLevelMethodVisitor.Callback {
            override fun accept() {
                enabled = true
            }
        })
        return mv2

//        return mv

    }

    override fun secondPass(classWriter: AsmClassWriter, classReader: ClassReader) {
        // do nothing
    }

}
