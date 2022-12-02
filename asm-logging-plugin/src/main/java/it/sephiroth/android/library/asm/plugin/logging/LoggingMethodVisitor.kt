package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.commons.utils.StringUtils
import org.gradle.api.logging.Logger
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.InstructionAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory


@Suppress("SpellCheckingInspection", "CanBeParameter", "unused")
class LoggingMethodVisitor(
    private val className: String,
    private val methodName: String,
    private val access: Int,
    private val descriptor: String,
    private val methodVisitor: InstructionAdapter
) :
    LocalVariablesSorter(ASM_VERSION, access, descriptor, methodVisitor), Opcodes {
//    MethodVisitor(ASM_VERSION, methodVisitor), Opcodes {

    private val simpleClassName = StringUtils.getSimpleClassName(className)
    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val tagName = "$simpleClassName:$methodName ->"

    private var enabled = false
    private var lineNumber: Int = 0

    override fun visitLineNumber(line: Int, start: Label?) {
        logger.lifecycle("$tagName visitLineNumber($line)")
        lineNumber = line
        super.visitLineNumber(line, start)
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
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

    @Suppress("UNUSED_PARAMETER")
    private fun visitTrunkMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ): Boolean {
        val result = Constants.Trunk.replace(name, descriptor, opcode)
        if (null != result) {
            logger.lifecycle("$tagName Adding Trunk replacement at line $lineNumber")
            val priority = result.first
            val newMethod = result.second

            if (newMethod == Constants.Trunk.LOG_ONCE_TAG || newMethod == Constants.Trunk.LOG_ONCE_THROWABLE_ONLY_TAG || newMethod == Constants.Trunk.LOG_ONCE_THROWABLE_TAG) {
                super.visitLdcInsn(simpleClassName)
                AsmVisitorUtils.visitInt(methodVisitor, lineNumber)

                super.visitMethodInsn(
                    newMethod.opcode,
                    newMethod.className,
                    newMethod.methodName,
                    newMethod.descriptor,
                    false
                )
            } else {
                AsmVisitorUtils.visitInt(methodVisitor, priority)
                super.visitLdcInsn(simpleClassName)
                AsmVisitorUtils.visitInt(methodVisitor, lineNumber)
                super.visitMethodInsn(
                    newMethod.opcode,
                    newMethod.className,
                    newMethod.methodName,
                    newMethod.descriptor,
                    false
                )
            }
            return true
        }
        return false
    }
}
