package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.commons.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodAnnotationData
import org.gradle.api.logging.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.InstructionAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

internal object MethodVisitorUtil {
    private val logger: Logger = LoggerFactory.getLogger(MethodVisitorUtil::class.java) as Logger
    private val tag = it.sephiroth.android.library.asm.commons.Constants.makeTag(MethodVisitorUtil::class.java)

    /**
     * Create the needed code injection in order to add the "ParamsLogger" invocation
     */
    fun printMethodStart(
        localVariablesSorter: LocalVariablesSorter,
        methodVisitor: InstructionAdapter,
        methodData: MethodAnnotationData,
    ): Int? {
        var timingStartVarIndex: Int? = null

        if (methodData.debugEnter) {
            logger.lifecycle("$tag printMethodStart(${methodData.simpleClassName}:${methodData.methodName})")

            methodVisitor.visitTypeInsn(Opcodes.NEW, Constants.JavaTypes.TYPE_PARAMS_LOGGER)
            methodVisitor.visitInsn(Opcodes.DUP)
            methodVisitor.visitLdcInsn(methodData.finalTag)                 // [1] tag (String)
            methodVisitor.visitLdcInsn(methodData.methodName)                            // [2] methodName (String)
            AsmVisitorUtils.visitInt(methodVisitor, methodData.debugArguments)     // [3] debugType (int)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V", false)

            //if (methodData.debugArguments != DebugArguments.None.value) {
            methodData.methodParams.forEach { parameter ->
                val name = parameter.name
                val descriptor = parameter.descriptor
                val index = parameter.index
                if (methodData.debugArguments == DebugArguments.None.value) {
                    val fullyDesc = String.format("(Ljava/lang/String;)L%s;", Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                    printMethodArgument(localVariablesSorter, name, fullyDesc)
                } else {
                    val opcode = AsmVisitorUtils.getLoadOpcodeFromDesc(descriptor)
                    val fullyDesc = String.format("(Ljava/lang/String;%s)L%s;", descriptor, Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                    printMethodArgumentAndValue(localVariablesSorter, index, opcode, name, fullyDesc)
                }
            }
            //}

            AsmVisitorUtils.visitInt(methodVisitor, methodData.logLevel)
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "print", "(I)V", false)
        }

        // Insert a start local variable containing the current time in ms
        if (methodData.debugExit) {
            logger.lifecycle("$tag ${methodData.simpleClassName}:${methodData.methodName} -> adding currentTimeMillis variable")
            timingStartVarIndex = localVariablesSorter.newLocal(Type.LONG_TYPE)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            methodVisitor.visitVarInsn(Opcodes.LSTORE, timingStartVarIndex)
        }

        return timingStartVarIndex
    }


    /**
     * Create the needed code injection in order to add the "MethodResultLogger" invocation
     */
    fun printMethodEnd(
        localVariablesSorter: LocalVariablesSorter,
        methodVisitor: InstructionAdapter,
        timingStartVarIndex: Int?,
        opcode: Int,
        methodData: MethodAnnotationData
    ) {
        if (null == timingStartVarIndex) {
            logger.warn("$tag ${methodData.simpleClassName}:${methodData.methodName} -> timingStartVarIndex should not be null here")
            return
        }

        logger.lifecycle("$tag printMethodEnd(${methodData.simpleClassName}:${methodData.methodName}) -> timingStartVarIndex=$timingStartVarIndex")
        logger.lifecycle("methodData=$methodData")
        logger.lifecycle("method descriptor = ${methodData.descriptor}")

        var returnType: Type = Type.getReturnType(methodData.descriptor)
        logger.lifecycle("returnType=$returnType")

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
            resultTempValIndex = localVariablesSorter.newLocal(returnType)
            var storeOpcocde: Int = AsmVisitorUtils.getStoreOpcodeFromType(returnType)
            if (opcode == Opcodes.ATHROW) {
                storeOpcocde = Opcodes.ASTORE
            }
            methodVisitor.visitVarInsn(storeOpcocde, resultTempValIndex)
        }

        logger.debug("$tag ${methodData.simpleClassName}:${methodData.methodName} -> opcode=$opcode, returnType=$returnType, returnDesc=$returnDesc")

        // Timing: parameter1 parameter2
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        methodVisitor.visitVarInsn(Opcodes.LLOAD, timingStartVarIndex!!)
        methodVisitor.visitInsn(Opcodes.LSUB)
        val index = localVariablesSorter.newLocal(Type.LONG_TYPE)
        methodVisitor.visitVarInsn(Opcodes.LSTORE, index)

        AsmVisitorUtils.visitInt(methodVisitor, methodData.logLevel)   // logLevel (int)
        methodVisitor.visitLdcInsn(methodData.debugArguments)          // printArguments (int)
        methodVisitor.visitLdcInsn(methodData.finalTag)                // className/tag (String)
        methodVisitor.visitLdcInsn(methodData.methodName)                    // methodName (String)
        methodVisitor.visitVarInsn(Opcodes.LLOAD, index)               // costedMillis (long)

        // Last parameter type is based on the method return type
        if (returnType != Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
            var loadOpcode: Int = AsmVisitorUtils.getLoadOpcodeFromType(returnType)
            if (opcode == Opcodes.ATHROW) {
                loadOpcode = Opcodes.ALOAD
                returnDesc = "L${Constants.JavaTypes.TYPE_OBJECT};"
            }
            methodVisitor.visitVarInsn(loadOpcode, resultTempValIndex)
            val formatDesc = String.format("(IILjava/lang/String;Ljava/lang/String;J%s)V", returnDesc)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.JavaTypes.TYPE_RESULT_LOGGER, "print", formatDesc, false)
            methodVisitor.visitVarInsn(loadOpcode, resultTempValIndex)
        } else {
            // mv.visitInsn(Opcodes.ACONST_NULL) // use null as result variable
            methodVisitor.visitLdcInsn("void") // use 'void' instead of passing a null object
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKESTATIC, Constants.JavaTypes.TYPE_RESULT_LOGGER, "print",
                "(IILjava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V", false
            )
        }
    }

    /**
     * Print only param name
     */
    private fun printMethodArgument(methodVisitor: MethodVisitor, name: String, descriptor: String) {
        methodVisitor.visitLdcInsn(name)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }

    /**
     * Print both param name and its value
     */
    private fun printMethodArgumentAndValue(methodVisitor: MethodVisitor, localIndex: Int, opcode: Int, name: String, descriptor: String) {
        methodVisitor.visitLdcInsn(name)
        methodVisitor.visitVarInsn(opcode, localIndex)
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }

}
