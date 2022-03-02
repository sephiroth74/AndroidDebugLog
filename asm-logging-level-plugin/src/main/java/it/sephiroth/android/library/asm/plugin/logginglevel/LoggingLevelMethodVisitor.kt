package it.sephiroth.android.library.asm.plugin.logginglevel

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.plugin.logginglevel.vo.LoggingPluginData
import org.gradle.api.logging.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory


@Suppress("SpellCheckingInspection", "CanBeParameter", "unused")
class LoggingLevelMethodVisitor(
    private val className: String,
    private val simpleClassName: String,
    private val methodName: String,
    private val descriptor: String,
    methodVisitor: MethodVisitor?,
    private val pluginData: LoggingPluginData,
    private val callback: Callback?
) : MethodVisitor(it.sephiroth.android.library.asm.plugin.core.Constants.ASM_VERSION, methodVisitor), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val tagName = "[${Constants.makeTag(this)}] $simpleClassName:$methodName ->"
    private var enabled = false

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        var handled = false

        if (opcode == Opcodes.INVOKEVIRTUAL) {
            // timber.log.Timber

            // 1. Replace Timber$Tree isLoggable with NullLogger.isLoggable
//            if (Constants.TimberTree.CLASS_NAME == owner && Constants.TimberTree.IS_LOGGABLE.matches(name, descriptor, opcode)) {
//                val newMethod = Constants.NullLogger.IS_LOGGABLE
//                AsmVisitorUtils.visitInt(mv, pluginData.minLogLevel.value)
//                super.visitMethodInsn(newMethod.opcode, Constants.NullLogger.CLASS_NAME, newMethod.methodName, newMethod.descriptor, false)
//                logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:$newMethod")
//                handled = true
//            }

        } else if (opcode == Opcodes.INVOKESTATIC) {
            // android.util.Log
            if (owner == Constants.AndroidLog.CLASS_NAME) { // method call to Log.*
                logger.debug("$tagName checking opcode=$opcode, $owner:$name$descriptor")

                if (Constants.AndroidLog.IS_LOGGABLE.matches(name, descriptor, opcode)) {
                    // 2. replace Log.isLoggable calls with NullLogger.isLoggable
                    val newMethod = Constants.LoggingLevel.IS_LOGGABLE
                    super.visitMethodInsn(newMethod.opcode, newMethod.className, newMethod.methodName, newMethod.descriptor, false)
                    logger.lifecycle("$tagName replaced $owner:$name with $newMethod")
                    handled = true

                } else if (Constants.AndroidLogMethods.isIndirectMethod(name, descriptor)) { // direct call to Log.v, Log.e, Log.w, etc..
                    // Replace Log.w, Log.v, etc calls with NullLogger.w, NullLogger.v,e tc
                    if (shouldAndroidLogMethodBeReplaced(name, descriptor, pluginData.minLogLevel)) {
                        super.visitMethodInsn(opcode, Constants.NullLogger.CLASS_NAME, name, descriptor, false)
                        logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:$name$descriptor")
                        handled = true
                    }
                } else if (Constants.AndroidLog.PRINTLN.matches(name, descriptor, opcode)) { // direct call to Log.println
                    // 4. use NullLogger.println(int, string, string) instead
                    val newMethod = Constants.NullLogger.PRINTLN

                    super.visitMethodInsn(opcode, newMethod.className, newMethod.methodName, newMethod.descriptor, false)
                    logger.lifecycle("$tagName replaced $owner:$name with $newMethod")
                    handled = true
                } else {
                    logger.debug("$tagName not handled call to $owner::$name")
                }
            }
        }

        if (handled) {
            logger.debug("$tagName replaced call $owner::$name")
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }

    private fun shouldAndroidLogMethodBeReplaced(name: String?, descriptor: String?, minLevel: AndroidLogLevel): Boolean {
        return Constants.AndroidLogMethods.contains(name, descriptor)?.let { method -> method.level < minLevel } ?: false
    }

    override fun visitEnd() {
        super.visitEnd()
        if (enabled) {
            callback?.accept()
        }
    }

    interface Callback {
        fun accept()
    }
}
