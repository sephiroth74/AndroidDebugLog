package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.*
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

class PreMethodVisitor(
    private val methodName: String,
    private val className: String,
    access: Int,
    descriptor: String,
    methodVisitor: MethodVisitor,
    private val methodData: MethodData,
    classMethodData: MethodData?,
    private val callback: Callback?
) : LocalVariablesSorter(it.sephiroth.android.library.asm.plugin.core.Constants.ASM_VERSION, access, descriptor, methodVisitor), Opcodes {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger
    private val tagName = "[${Constants.makeTag(this)}]"

    private val labels = mutableListOf<Label>()
    private val parameters = mutableListOf<MethodParameter>()
    private var enabled = false

    init {
        logger.debug("$tagName visiting method $className::$methodName")

        classMethodData?.let {
            methodData.copyFrom(it)
            enabled = it.enabled

            if (!enabled) {
                logger.lifecycle("$tagName $className:$methodName -> should be skipped")
            }
        }
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.debug("$tagName ${className}:$methodName visitAnnotation $descriptor")
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG};") {
            val av2 = PreAnnotationVisitor(av, methodData, object : PreAnnotationVisitor.Callback {
                override fun accept(methodData: MethodData) {
                    enabled = methodData.enabled
                    logger.debug("$tagName ${className}:${methodName} now is enabled = $enabled (${methodData.enabled})")
                }
            })
            return av2
        } else if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_SKIP};") {
            enabled = false
            methodData.skipMethod = true
            logger.lifecycle("$tagName $className:$methodName -> should be skipped")
        }
        return av
    }

    override fun visitLocalVariable(name: String, descriptor: String, signature: String?, start: Label, end: Label, index: Int) {
        if (!methodData.skipMethod && enabled && "this" != name && start == labels.first()) {
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
        if (enabled && !methodData.skipMethod) {
            callback?.accept(methodData, parameters)
        }
    }

    interface Callback {
        fun accept(methodData: MethodData, params: List<MethodParameter>)
    }
}
