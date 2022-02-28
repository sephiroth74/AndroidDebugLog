package it.sephiroth.android.library.asm.logging

import it.sephiroth.android.library.asm.core.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.logging.vo.LoggingPluginData
import org.gradle.api.logging.Logger
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory


@Suppress("SpellCheckingInspection", "CanBeParameter", "unused")
class LoggingMethodVisitor(
    private val className: String,
    private val simpleClassName: String,
    private val methodName: String,
    private val descriptor: String,
    methodVisitor: MethodVisitor?,
    private val pluginData: LoggingPluginData,
    private val callback: Callback? = null
) : MethodVisitor(it.sephiroth.android.library.asm.core.Constants.ASM_VERSION, methodVisitor), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val tagName = "[${Constants.makeTag(this)}] $simpleClassName:$methodName ->"

    private val labels = mutableListOf<Label>()
    private var enabled = false
    private var lineNumber: Int = 0

    override fun visitLineNumber(line: Int, start: Label?) {
        lineNumber = line
        super.visitLineNumber(line, start)
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        var handled = false

        if (opcode == Opcodes.INVOKESTATIC) {
            if (owner == Constants.SimpleLog.CLASS_NAME) {
//                logger.lifecycle("$tagName calling: $owner, $name, $descriptor")
                if (visitSimpleLogMethodInsn(opcode, owner, name, descriptor, isInterface)) {
                    logger.lifecycle("$tagName replaced $owner:$name$descriptor")
                    handled = true
                } else {
                    logger.lifecycle("doesn't match [$owner:$name$descriptor]")
                }


            }
        } else if (opcode == Opcodes.INVOKEVIRTUAL) {
            // timber.log.Timber

            // 1. Replace Timber$Tree isLoggable with NullLogger.isLoggable
            if (Constants.TimberTree.CLASS_NAME == owner) {
//                val newMethod = Constants.NullLogger.IS_LOGGABLE
//                AsmVisitorUtils.visitInt(mv, pluginData.minLogLevel.value)
//                super.visitMethodInsn(newMethod.opcode, Constants.NullLogger.CLASS_NAME, newMethod.methodName, newMethod.descriptor, false)
//                logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:$newMethod")
//                handled = true
            }
        }

//        } else if (opcode == Opcodes.INVOKESTATIC) {
        // android.util.SimpleLog
//            if (owner == Constants.AndroidLog.CLASS_NAME) { // method call to SimpleLog.*
//                logger.debug("[$tagName] ${className}:$methodName -> checking opcode=$opcode, $owner:$name$descriptor")
//
//                if (Constants.AndroidLog.IS_LOGGABLE.matches(name, descriptor, opcode)) {
//                    // 2. replace SimpleLog.isLoggable calls with NullLogger.isLoggable
//                    val newMethod = Constants.NullLogger.IS_LOGGABLE_TAG
//                    AsmVisitorUtils.visitInt(mv, pluginData.minLogLevel.value)
//                    super.visitMethodInsn(newMethod.opcode, Constants.NullLogger.CLASS_NAME, newMethod.methodName, newMethod.descriptor, false)
//                    logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:$newMethod")
//                    handled = true
//
//                } else if (Constants.AndroidLogMethods.isIndirectMethod(name, descriptor)) { // direct call to SimpleLog.v, SimpleLog.e, SimpleLog.w, etc..
//                    // Replace SimpleLog.w, SimpleLog.v, etc calls with NullLogger.w, NullLogger.v,e tc
//                    if (shouldAndroidLogMethodBeReplaced(name, descriptor, pluginData.minLogLevel)) {
//                        super.visitMethodInsn(opcode, Constants.NullLogger.CLASS_NAME, name, descriptor, false)
//                        logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:$name$descriptor")
//                        handled = true
//                    }
//                } else if (Constants.AndroidLog.PRINTLN.matches(name, descriptor, opcode)) { // direct call to SimpleLog.println
//                    // 4. use NullLogger.println(int, string, string, int) instead, passing current minLevel
//                    val newMethod = Constants.NullLogger.PRINTLN_MIN_LEVEL
//                    AsmVisitorUtils.visitInt(mv, pluginData.minLogLevel.value)
//                    super.visitMethodInsn(opcode, Constants.NullLogger.CLASS_NAME, newMethod.methodName, newMethod.descriptor, false)
//
//                    logger.lifecycle("$tagName replaced $owner:$name with ${Constants.NullLogger.SIMPLE_CLASS_NAME}:${newMethod.methodName}${newMethod.descriptor}")
//                    handled = true
//                } else {
//                    logger.debug("$tagName not handled call to $owner::$name")
//                }
//            }
//        }

        if (handled) {
            logger.debug("$tagName $className:$methodName -> replaced call $owner::$name")
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }

    override fun visitLabel(label: Label) {
        labels.add(label)
        super.visitLabel(label)
    }

    override fun visitEnd() {
        super.visitEnd()
        if (enabled) {
            callback?.accept()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun visitSimpleLogMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean): Boolean {
        val result = Constants.SimpleLog.replace(name, descriptor, opcode)
        if (null != result) {
            val priority = result.first
            val newMethod = result.second
            AsmVisitorUtils.visitInt(this, priority)
            super.visitLdcInsn("$simpleClassName[$lineNumber]")
            super.visitMethodInsn(newMethod.opcode, newMethod.className, newMethod.methodName, newMethod.descriptor, false)
            return true
        }
        return false
    }

    interface Callback {
        fun accept()
    }
}
