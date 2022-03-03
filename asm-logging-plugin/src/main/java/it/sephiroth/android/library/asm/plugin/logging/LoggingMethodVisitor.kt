package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.plugin.logging.vo.LoggingPluginData
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
) : MethodVisitor(it.sephiroth.android.library.asm.plugin.core.Constants.ASM_VERSION, methodVisitor), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val tagName = "[${Constants.makeTag(this)}] $simpleClassName:$methodName ->"

    private var enabled = false
    private var lineNumber: Int = 0

    override fun visitLineNumber(line: Int, start: Label?) {
        lineNumber = line
        super.visitLineNumber(line, start)
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        var handled = false

        if (opcode == Opcodes.INVOKESTATIC) {
            if (owner == Constants.Trunk.CLASS_NAME) {
                // Replace Trunk
                handled = visitTrunkMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }

        if (handled) {
            logger.info("$tagName replaced call $owner::$name")
            enabled = true
        } else {
            logger.debug("$tagName not handled $owner:$name$descriptor (opcode=$opcode) in $className::$methodName[$lineNumber]")
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        if (enabled) {
            callback?.accept()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun visitTrunkMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean): Boolean {
        val result = Constants.Trunk.replace(name, descriptor, opcode)
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
