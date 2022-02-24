package it.sephiroth.android.library.asm.debuglog.asm.post

import it.sephiroth.android.library.asm.core.Constants
import it.sephiroth.android.library.asm.core.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

@Suppress("SpellCheckingInspection")
class PostMethodVisitor(
    private val className: String,
    private val parameters: List<MethodParameter>,
    private val methodData: MethodData,
    access: Int,
    descriptor: String,
    methodVisitor: MethodVisitor
) : LocalVariablesSorter(Constants.ASM_VERSION, access, descriptor, methodVisitor), Opcodes {

    private val logger = LoggerFactory.getLogger(PostMethodVisitor::class.java) as Logger
    private val tagName: String = "[${it.sephiroth.android.library.asm.debuglog.Constants.makeTag(this)}]"
    private var timingStartVarIndex: Int? = null

    override fun visitCode() {
        super.visitCode()
        printMethodStart()
    }

    override fun visitInsn(opcode: Int) {
        if (methodData.debugResult && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW)) {
            printMethodEnd(opcode)
        }
        super.visitInsn(opcode)
    }

    /**
     * Create the needed code injection in order to add the "MethodResultLogger" invocation
     */
    private fun printMethodEnd(opcode: Int) {
        logger.lifecycle("$tagName ($className:${methodData.name}) Creating output logger injection")

        if (null == timingStartVarIndex) {
            logger.warn("$tagName timingStartVarIndex should not be null here")
            return
        }

        var returnType: Type = Type.getReturnType(methodData.descriptor)
        var returnDesc: String = methodData.descriptor.substring(methodData.descriptor.indexOf(")") + 1)
        if (returnDesc.startsWith("[") || returnDesc.startsWith("L")) {
            returnDesc = "L${Constants.JavaTypes.TYPE_OBJECT};"
        }

        // Store origin return value
        var resultTempValIndex = -1
        if (returnType != Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
            if (opcode == Opcodes.ATHROW) {
                returnType = Type.getType("L${Constants.JavaTypes.TYPE_OBJECT};")
            }
            resultTempValIndex = newLocal(returnType)
            var storeOpcocde: Int = AsmVisitorUtils.getStoreOpcodeFromType(returnType)
            if (opcode == Opcodes.ATHROW) {
                storeOpcocde = Opcodes.ASTORE
            }
            mv.visitVarInsn(storeOpcocde, resultTempValIndex)
        }

        logger.debug("$tagName $className:${methodData.name} opcode=$opcode, returnType=$returnType, returnDesc=$returnDesc")

        // Timing: parameter1 parameter2
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        mv.visitVarInsn(Opcodes.LLOAD, timingStartVarIndex!!)
        mv.visitInsn(Opcodes.LSUB)
        val index = newLocal(Type.LONG_TYPE)
        mv.visitVarInsn(Opcodes.LSTORE, index)

        AsmVisitorUtils.visitInt(mv, methodData.logLevel)   // logLevel (int)
        mv.visitLdcInsn(methodData.debugArguments)          // printArguments (int)
        mv.visitLdcInsn(methodData.finalTag)                // className/tag (String)
        mv.visitLdcInsn(methodData.name)                    // methodName (String)
        mv.visitVarInsn(Opcodes.LLOAD, index)               // costedMillis (long)

        // Last parameter type is based on the method return type
        if (returnType != Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
            var loadOpcode: Int = AsmVisitorUtils.getLoadOpcodeFromType(returnType)
            if (opcode == Opcodes.ATHROW) {
                loadOpcode = Opcodes.ALOAD
                returnDesc = "L${it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_OBJECT};"
            }
            mv.visitVarInsn(loadOpcode, resultTempValIndex)
            val formatDesc = String.format("(IILjava/lang/String;Ljava/lang/String;J%s)V", returnDesc)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_RESULT_LOGGER, "print", formatDesc, false)
            mv.visitVarInsn(loadOpcode, resultTempValIndex)
        } else {
            // mv.visitInsn(Opcodes.ACONST_NULL) // use null as result variable
            mv.visitLdcInsn("void") // use 'void' instead of passing a null object
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_RESULT_LOGGER, "print",
                "(IILjava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V", false
            )
        }
    }

    /**
     * Create the needed code injection in order to add the "ParamsLogger" invocation
     */
    private fun printMethodStart() {
        logger.lifecycle("$tagName ($className:${methodData.name}) Creating input logger injection")
        // val lineNumber: Int = methodData.lineNumber?.minus(1) ?: 0
        mv.visitTypeInsn(Opcodes.NEW, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER)
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn(methodData.finalTag)                 // [1] tag (String)
        mv.visitLdcInsn(methodData.name)                            // [2] methodName (String)
        AsmVisitorUtils.visitInt(mv, methodData.debugArguments)     // [3] debugType (int)

        // [4] lineNumber (int)
        // if (lineNumber > 0) {
        //    ASMVisitorUtils.visitInt(mv, lineNumber)
        //} else {
        //    mv.visitInsn(Opcodes.ACONST_NULL)
        //}

        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER, "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V", false)

        //if (methodData.debugArguments != DebugArguments.None.value) {
        parameters.forEach { parameter ->
            val name = parameter.name
            val descriptor = parameter.descriptor
            val index = parameter.index
            if (methodData.debugArguments == it.sephiroth.android.library.asm.debuglog.DebugArguments.None.value) {
                val fullyDesc = String.format("(Ljava/lang/String;)L%s;", it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                printMethodArgument(name, fullyDesc)
            } else {
                val opcode = AsmVisitorUtils.getLoadOpcodeFromDesc(descriptor)
                val fullyDesc = String.format("(Ljava/lang/String;%s)L%s;", descriptor, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                printMethodArgumentAndValue(index, opcode, name, fullyDesc)
            }
        }
        //}

        AsmVisitorUtils.visitInt(mv, methodData.logLevel)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER, "print", "(I)V", false)

        // Insert a start local variable containing the current time in ms
        if (methodData.debugResult) {
            logger.debug("[$tagName] ($className:${methodData.name}) Adding currentTimeMillis variable")
            timingStartVarIndex = newLocal(Type.LONG_TYPE)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            mv.visitVarInsn(Opcodes.LSTORE, timingStartVarIndex!!)
        }
    }

    /**
     * Print only param name
     */
    private fun printMethodArgument(name: String, descriptor: String) {
        mv.visitLdcInsn(name)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }

    /**
     * Print both param name and its value
     */
    private fun printMethodArgumentAndValue(localIndex: Int, opcode: Int, name: String, descriptor: String) {
        mv.visitLdcInsn(name)
        mv.visitVarInsn(opcode, localIndex)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }
}