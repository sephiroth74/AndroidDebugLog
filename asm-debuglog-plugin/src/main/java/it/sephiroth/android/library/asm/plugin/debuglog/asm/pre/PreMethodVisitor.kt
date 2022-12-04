package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.ClassAnnotationData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodAnnotationData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.*
import org.objectweb.asm.commons.InstructionAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@Suppress("unused", "CanBeParameter")
class PreMethodVisitor(
    private val methodName: String,
    private val className: String,
    private val access: Int,
    private val name: String,
    private val descriptor: String,
    private val methodVisitor: MethodVisitor,
    private val classData: ClassAnnotationData?,
    private val pluginData: DebugLogPluginParameters
) :
    MethodVisitor(ASM_VERSION, null), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(PreMethodVisitor::class.java) as Logger

    private val tag = makeTag(this)
    private val labels = mutableListOf<Label>()

    // final method writer
    private val instructorVisitor = InstructionAdapter(methodVisitor)
    private val finalMethodVisitor = LocalVariablesSorter(access, descriptor, instructorVisitor)

    private val secondPass = AtomicBoolean(false)
    private val timingStartVarIndex = AtomicReference<Int?>(null)

    private var methodData: MethodAnnotationData? = null

    private val deferredCalls = mutableListOf<(() -> Unit)>()

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        if (secondPass.get()) return finalMethodVisitor.visitAnnotation(descriptor, visible)

        logger.lifecycle("$tag visitAnnotation(descriptor=$descriptor)")
        val av: AnnotationVisitor? = super.visitAnnotation(descriptor, visible)
        return when (descriptor) {

            "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG};" -> {
                val methodData = if (null != classData) MethodAnnotationData(classData, methodName, descriptor)
                else MethodAnnotationData(pluginData, className, methodName, this.descriptor)
                this.methodData = methodData
                PreAnnotationVisitor(av, methodData)
            }

            "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_SKIP};" -> {
                logger.debug("$tag $className:$methodName debugLog skipped")
                val methodData = MethodAnnotationData(pluginData, className, methodName, this.descriptor).also { it.shouldSkip = true }
                this.methodData = methodData
                PreAnnotationVisitor(av, methodData)
            }

            else -> {
                finalMethodVisitor.visitAnnotation(descriptor, visible)
            }
        }
    }

    override fun visitLocalVariable(name: String, descriptor: String, signature: String?, start: Label, end: Label, index: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitLocalVariable(name, descriptor, signature, start, end, index)

        logger.lifecycle("$tag visitLocalVariable(name=$name)")

        if (methodData?.isEnabled() == true && "this" != name && start == labels.first()) {
            val type = Type.getType(descriptor)
            if (type.sort == Type.OBJECT || type.sort == Type.ARRAY) {
                methodData?.methodParams?.add(MethodParameter(name, "L${Constants.JavaTypes.TYPE_OBJECT};", index))
            } else {
                methodData?.methodParams?.add(MethodParameter(name, descriptor, index))
            }
        }
        deferredCalls.add { visitLocalVariable(name, descriptor, signature, start, end, index) }
    }

    override fun visitLabel(label: Label) {
        if (secondPass.get()) return finalMethodVisitor.visitLabel(label)

        logger.lifecycle("$tag visitLabel(label=$label)")
        labels.add(label)
        deferredCalls.add { visitLabel(label) }
    }

    override fun visitCode() {
        if (secondPass.get()) {
            finalMethodVisitor.visitCode()

            methodData.takeIf { it != null && it.isEnabled() }?.let { data ->
                timingStartVarIndex.set(MethodVisitorUtil.printMethodStart(finalMethodVisitor, instructorVisitor, data))
            }

            return
        }

        logger.lifecycle("$tag visitCode()")
        deferredCalls.add { visitCode() }
    }

    override fun visitParameter(name: String?, access: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitParameter(name, access)

        logger.lifecycle("$tag visitParameter($name)")
        deferredCalls.add { visitParameter(name, access) }
    }

    override fun visitAnnotationDefault(): AnnotationVisitor {
        logger.lifecycle("$tag visitAnnotationDefault()")
        return finalMethodVisitor.visitAnnotationDefault()
    }

    override fun visitTypeAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.lifecycle("$tag visitTypeAnnotation()")
        return finalMethodVisitor.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitAnnotableParameterCount(parameterCount: Int, visible: Boolean) {
        logger.lifecycle("$tag visitAnnotableParameterCount(parameterCount=$parameterCount)")
        finalMethodVisitor.visitAnnotableParameterCount(parameterCount, visible)
    }

    override fun visitParameterAnnotation(parameter: Int, descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.lifecycle("$tag visitParameterAnnotation(parameter=$parameter)")
        return finalMethodVisitor.visitParameterAnnotation(parameter, descriptor, visible)
    }

    override fun visitAttribute(attribute: Attribute?) {
        if (secondPass.get()) return finalMethodVisitor.visitAttribute(attribute)

        logger.lifecycle("$tag visitAttribute()")
        deferredCalls.add { visitAttribute(attribute) }
    }

    override fun visitFrame(type: Int, numLocal: Int, local: Array<out Any>?, numStack: Int, stack: Array<out Any>?) {
        if (secondPass.get()) return finalMethodVisitor.visitFrame(type, numLocal, local, numStack, stack)

        logger.lifecycle("$tag visitFrame(numLocal=$numLocal, numStack=$numStack)")
        deferredCalls.add { visitFrame(type, numLocal, local, numStack, stack) }
    }

    override fun visitInsn(opcode: Int) {
        if (secondPass.get()) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW)) {
                methodData.takeIf { it != null && it.isEnabled() && it.debugExit }?.let { data ->
                    MethodVisitorUtil.printMethodEnd(finalMethodVisitor, instructorVisitor, timingStartVarIndex.get(), opcode, data)
                }
            }
            return finalMethodVisitor.visitInsn(opcode)
        }

        logger.lifecycle("$tag visitInsn(opcode=$opcode)")
        deferredCalls.add { visitInsn(opcode) }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitIntInsn(opcode, operand)

        logger.lifecycle("$tag visitIntInsn(opcode=$opcode)")
        deferredCalls.add { visitIntInsn(opcode, operand) }
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitVarInsn(opcode, `var`)

        logger.lifecycle("$tag visitVarInsn(opcode=$opcode)")
        deferredCalls.add { visitVarInsn(opcode, `var`) }
    }

    override fun visitTypeInsn(opcode: Int, type: String?) {
        if (secondPass.get()) return finalMethodVisitor.visitTypeInsn(opcode, type)

        logger.lifecycle("$tag visitTypeInsn(opcode=$opcode)")
        deferredCalls.add { visitTypeInsn(opcode, type) }
    }

    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        if (secondPass.get()) return finalMethodVisitor.visitFieldInsn(opcode, owner, name, descriptor)

        logger.lifecycle("$tag visitFieldInsn(opcode=$opcode, name=$name)")
        deferredCalls.add { visitFieldInsn(opcode, owner, name, descriptor) }
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        if (secondPass.get()) return finalMethodVisitor.visitMethodInsn(opcode, owner, name, descriptor)

        logger.lifecycle("$tag visitMethodInsn(opcode=$opcode, name=$name) [DEPRECATED]")
        deferredCalls.add { visitMethodInsn(opcode, owner, name, descriptor) }
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        if (secondPass.get()) return finalMethodVisitor.visitMethodInsn(opcode, owner, name, descriptor, isInterface)

        logger.lifecycle("$tag visitMethodInsn(opcode=$opcode, name=$name)")
        deferredCalls.add { visitMethodInsn(opcode, owner, name, descriptor, isInterface) }
    }

    override fun visitInvokeDynamicInsn(name: String?, descriptor: String?, bootstrapMethodHandle: Handle?, vararg bootstrapMethodArguments: Any?) {
        if (secondPass.get()) return finalMethodVisitor.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)

        logger.lifecycle("$tag visitInvokeDynamicInsn(name=$name)")
        deferredCalls.add { visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments) }
    }

    override fun visitJumpInsn(opcode: Int, label: Label?) {
        if (secondPass.get()) return finalMethodVisitor.visitJumpInsn(opcode, label)

        logger.lifecycle("$tag visitJumpInsn()")
        deferredCalls.add { visitJumpInsn(opcode, label) }
    }

    override fun visitLdcInsn(value: Any?) {
        if (secondPass.get()) return finalMethodVisitor.visitLdcInsn(value)

        logger.lifecycle("$tag visitLdcInsn(value=$value)")
        deferredCalls.add { visitLdcInsn(value) }
    }

    override fun visitIincInsn(`var`: Int, increment: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitIincInsn(`var`, increment)

        logger.lifecycle("$tag visitIincInsn(var=$`var`)")
        deferredCalls.add { visitIincInsn(`var`, increment) }
    }

    override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label?, vararg labels: Label?) {
        if (secondPass.get()) return finalMethodVisitor.visitTableSwitchInsn(min, max, dflt, *labels)

        logger.lifecycle("$tag visitTableSwitchInsn()")
        deferredCalls.add { visitTableSwitchInsn(min, max, dflt, *labels) }
    }

    override fun visitLookupSwitchInsn(dflt: Label?, keys: IntArray?, labels: Array<out Label>?) {
        if (secondPass.get()) return finalMethodVisitor.visitLookupSwitchInsn(dflt, keys, labels)

        logger.lifecycle("$tag visitLookupSwitchInsn()")
        deferredCalls.add { visitLookupSwitchInsn(dflt, keys, labels) }
    }

    override fun visitMultiANewArrayInsn(descriptor: String?, numDimensions: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitMultiANewArrayInsn(descriptor, numDimensions)

        logger.lifecycle("$tag visitMultiANewArrayInsn()")
        deferredCalls.add { visitMultiANewArrayInsn(descriptor, numDimensions) }
    }

    override fun visitInsnAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.lifecycle("$tag visitInsnAnnotation()")
        return finalMethodVisitor.visitInsnAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitTryCatchBlock(start: Label?, end: Label?, handler: Label?, type: String?) {
        if (secondPass.get()) return finalMethodVisitor.visitTryCatchBlock(start, end, handler, type)

        logger.lifecycle("$tag visitTryCatchBlock()")
        deferredCalls.add { visitTryCatchBlock(start, end, handler, type) }
    }

    override fun visitTryCatchAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.lifecycle("$tag visitTryCatchAnnotation()")
        return finalMethodVisitor.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitLocalVariableAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        start: Array<out Label>?,
        end: Array<out Label>?,
        index: IntArray?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor {
        logger.lifecycle("$tag visitLocalVariableAnnotation()")
        return finalMethodVisitor.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible)
    }

    override fun visitLineNumber(line: Int, start: Label?) {
        if (secondPass.get()) return finalMethodVisitor.visitLineNumber(line, start)

        logger.lifecycle("$tag visitLineNumber(line=$line)")
        deferredCalls.add { visitLineNumber(line, start) }
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        if (secondPass.get()) return finalMethodVisitor.visitMaxs(maxStack, maxLocals)

        logger.lifecycle("$tag visitMaxs(maxStack=$maxStack, maxLocals=$maxLocals)")
        deferredCalls.add { visitMaxs(maxStack, maxLocals) }
    }

    override fun visitEnd() {
        if (secondPass.get()) return finalMethodVisitor.visitEnd()

        logger.lifecycle("$tag visitEnd(enabled=${methodData?.isEnabled()})")
        deferredCalls.add { visitEnd() }
        secondPass.set(true)

        val total = deferredCalls.size
        deferredCalls.forEachIndexed { index, function ->
            logger.lifecycle("$tag executing $index of $total deferred calls...")
            function.invoke()
        }
    }
}
