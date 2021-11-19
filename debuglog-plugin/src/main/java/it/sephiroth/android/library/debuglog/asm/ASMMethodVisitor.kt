package it.sephiroth.android.library.debuglog.asm

import it.sephiroth.android.library.debuglog.Constants
import it.sephiroth.android.library.debuglog.DebugLogPlugin
import it.sephiroth.android.library.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.debuglog.asm.vo.MethodParameter

import org.gradle.api.logging.Logger
import org.objectweb.asm.*
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

class ASMMethodVisitor(
        private val methodName: String,
        private val className: String,
        access: Int,
        descriptor: String,
        methodVisitor: MethodVisitor,
        private val methodData: MethodData,
        private val classMethodData: MethodData?,
        private val callback: Callback?
) : LocalVariablesSorter(Constants.ASM_VERSION, access, descriptor, methodVisitor), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val labels = mutableListOf<Label>()
    private val parameters = mutableListOf<MethodParameter>()

    private var enabled = false

    init {
        logger.debug("[$TAG] visiting method $className::$methodName")

        classMethodData?.let {
            methodData.copyFrom(it)
            enabled = it.enabled
        }
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.debug("[$TAG] ${className}:$methodName visitAnnotation $descriptor")
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG};") {
            val av2 = ASMAnnotationVisitor(av, methodData, object : ASMAnnotationVisitor.Callback {
                override fun accept(methodData: MethodData) {
                    enabled = methodData.enabled
                    logger.debug("[$TAG] ${className}:${methodName} now is enabled = $enabled (${methodData.enabled})")
                }
            })
            return av2
        }
        return av
    }

    override fun visitLocalVariable(name: String, descriptor: String, signature: String?, start: Label, end: Label, index: Int) {
        if (enabled && "this" != name && start == labels.first()) {
            logger.debug("[$TAG] {}:{} visitLocalVariable({}, {})", className, methodName, name, signature)
            val type = Type.getType(descriptor)
            if (type.sort == Type.OBJECT || type.sort == Type.ARRAY) {
                parameters.add(MethodParameter(name, "L${Constants.JavaTypes.TYPE_OBJECT};", index))
            } else {
                parameters.add(MethodParameter(name, descriptor, index))
            }
        }
        super.visitLocalVariable(name, descriptor, signature, start, end, index)
    }

    override fun visitLabel(label: Label) {
        labels.add(label)
        super.visitLabel(label)
    }

    override fun visitEnd() {
        super.visitEnd()
        if (enabled) {
            callback?.accept(methodData, parameters)
        }
    }

    companion object {
        private const val TAG = "${DebugLogPlugin.TAG}:ASMMethodVisitor"
    }

    interface Callback {
        fun accept(methodData: MethodData, params: List<MethodParameter>)
    }
}
