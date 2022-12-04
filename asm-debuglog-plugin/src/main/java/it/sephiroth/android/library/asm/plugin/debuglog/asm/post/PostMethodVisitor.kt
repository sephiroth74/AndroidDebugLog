package it.sephiroth.android.library.asm.plugin.debuglog.asm.post

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.commons.utils.AsmVisitorUtils
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreMethodVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodAnnotationData
import org.gradle.api.logging.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.InstructionAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

@Suppress("SpellCheckingInspection", "CanBeParameter")
class PostMethodVisitor(
    private val classContext: ClassContext,
    private val methodVisitor: PreMethodVisitor,
    private val access: Int,
    private val name: String,
    private val descriptor: String
) : LocalVariablesSorter(ASM_VERSION, access, descriptor, InstructionAdapter(methodVisitor)), Opcodes {

    private val logger = LoggerFactory.getLogger(PostMethodVisitor::class.java) as Logger
    private val className: String = classContext.currentClassData.className
    private val tag: String = makeTag(this)
    private var timingStartVarIndex: Int? = null
    private var methodData: MethodAnnotationData? = null

    override fun visitCode() {
        super.visitCode()
//        logger.lifecycle("$tag visitCode($name) -> ${methodVisitor.methodData}")

//        val methodData = methodVisitor.methodData
//        if (null != methodData && methodData.isEnabled()) {
//            logger.lifecycle("$tag Should print method: ${methodData.simpleClassName}:${methodData.methodName}")
//            printMethodStart(methodData)
//        }
    }

    override fun visitEnd() {
        super.visitEnd()
//        logger.lifecycle("$tag visitEnd($name) -> ${methodVisitor.methodData}")
    }

    override fun visitInsn(opcode: Int) {
        val methodData = methodData
        if (null != methodData && methodData.isEnabled() && methodData.debugExit
            && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW)
        ) {
            printMethodEnd(opcode, methodData)
        }
        super.visitInsn(opcode)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        val av = super.visitAnnotation(descriptor, visible)
        return when (descriptor) {
            "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG};" -> null
            "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_SKIP};" -> null
            else -> av
        }
    }

    /**
     * Create the needed code injection in order to add the "MethodResultLogger" invocation
     */
    private fun printMethodEnd(opcode: Int, methodData: MethodAnnotationData) {
        if (null == timingStartVarIndex) {
            logger.warn("$tag $className:${methodData.methodName} -> timingStartVarIndex should not be null here")
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
            super.visitVarInsn(storeOpcocde, resultTempValIndex)
        }

        logger.debug("$tag $className:${methodData.methodName} -> opcode=$opcode, returnType=$returnType, returnDesc=$returnDesc")

        // Timing: parameter1 parameter2
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        super.visitVarInsn(Opcodes.LLOAD, timingStartVarIndex!!)
        super.visitInsn(Opcodes.LSUB)
        val index = newLocal(Type.LONG_TYPE)
        super.visitVarInsn(Opcodes.LSTORE, index)

        AsmVisitorUtils.visitInt(this as InstructionAdapter, methodData.logLevel)   // logLevel (int)
        super.visitLdcInsn(methodData.debugArguments)          // printArguments (int)
        super.visitLdcInsn(methodData.finalTag)                // className/tag (String)
        super.visitLdcInsn(methodData.methodName)                    // methodName (String)
        super.visitVarInsn(Opcodes.LLOAD, index)               // costedMillis (long)

        // Last parameter type is based on the method return type
        if (returnType != Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
            var loadOpcode: Int = AsmVisitorUtils.getLoadOpcodeFromType(returnType)
            if (opcode == Opcodes.ATHROW) {
                loadOpcode = Opcodes.ALOAD
                returnDesc = "L${Constants.JavaTypes.TYPE_OBJECT};"
            }
            super.visitVarInsn(loadOpcode, resultTempValIndex)
            val formatDesc = String.format("(IILjava/lang/String;Ljava/lang/String;J%s)V", returnDesc)
            super.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.JavaTypes.TYPE_RESULT_LOGGER, "print", formatDesc, false)
            super.visitVarInsn(loadOpcode, resultTempValIndex)
        } else {
            // mv.visitInsn(Opcodes.ACONST_NULL) // use null as result variable
            super.visitLdcInsn("void") // use 'void' instead of passing a null object
            super.visitMethodInsn(
                Opcodes.INVOKESTATIC, Constants.JavaTypes.TYPE_RESULT_LOGGER, "print",
                "(IILjava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V", false
            )
        }
    }

    /**
     * Create the needed code injection in order to add the "ParamsLogger" invocation
     */
    private fun printMethodStart(methodData: MethodAnnotationData) {
        if (methodData.debugEnter) {
            logger.lifecycle("$tag $className:${methodData.methodName} -> creating input logging injection")
            super.visitTypeInsn(Opcodes.NEW, Constants.JavaTypes.TYPE_PARAMS_LOGGER)
            super.visitInsn(Opcodes.DUP)
            super.visitLdcInsn(methodData.finalTag)                 // [1] tag (String)
            super.visitLdcInsn(methodData.methodName)                            // [2] methodName (String)
            AsmVisitorUtils.visitInt(methodVisitor as InstructionAdapter, methodData.debugArguments)     // [3] debugType (int)

            super.visitMethodInsn(Opcodes.INVOKESPECIAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V", false)

            //if (methodData.debugArguments != DebugArguments.None.value) {
            methodData.methodParams.forEach { parameter ->
                val name = parameter.name
                val descriptor = parameter.descriptor
                val index = parameter.index
                if (methodData.debugArguments == DebugArguments.None.value) {
                    val fullyDesc = String.format("(Ljava/lang/String;)L%s;", Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                    printMethodArgument(name, fullyDesc)
                } else {
                    val opcode = AsmVisitorUtils.getLoadOpcodeFromDesc(descriptor)
                    val fullyDesc = String.format("(Ljava/lang/String;%s)L%s;", descriptor, Constants.JavaTypes.TYPE_PARAMS_LOGGER)
                    printMethodArgumentAndValue(index, opcode, name, fullyDesc)
                }
            }
            //}

            AsmVisitorUtils.visitInt(methodVisitor, methodData.logLevel)
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "print", "(I)V", false)
        }

        // Insert a start local variable containing the current time in ms
        if (methodData.debugExit) {
            logger.lifecycle("[$tag] $className:${methodData.methodName} -> adding currentTimeMillis variable")
            timingStartVarIndex = newLocal(Type.LONG_TYPE)
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            super.visitVarInsn(Opcodes.LSTORE, timingStartVarIndex!!)
        }
    }

    /**
     * Print only param name
     */
    private fun printMethodArgument(name: String, descriptor: String) {
        super.visitLdcInsn(name)
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }

    /**
     * Print both param name and its value
     */
    private fun printMethodArgumentAndValue(localIndex: Int, opcode: Int, name: String, descriptor: String) {
        super.visitLdcInsn(name)
        super.visitVarInsn(opcode, localIndex)
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Constants.JavaTypes.TYPE_PARAMS_LOGGER, "append", descriptor, false)
    }
}
