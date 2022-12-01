package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.*
import org.objectweb.asm.commons.LocalVariablesSorter
import org.slf4j.LoggerFactory

@Suppress("unused")
class PreMethodVisitor(
    private val methodName: String,
    private val className: String,
    private val access: Int,
    private val descriptor: String,
    private val methodVisitor: MethodVisitor?,
    private val methodData: MethodData,
    private val callback: ((MethodData, List<MethodParameter>) -> Unit)? = null
) :
    LocalVariablesSorter(ASM_VERSION, access, descriptor, methodVisitor), Opcodes {
    private val logger: Logger = LoggerFactory.getLogger(PreMethodVisitor::class.java) as Logger
    private val tagName = makeTag(this)
    private val labels = mutableListOf<Label>()
    private val parameters = mutableListOf<MethodParameter>()
    private var enabled = false

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        val av = super.visitAnnotation(descriptor, visible)

        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG};") {
            val av2 = PreAnnotationVisitor(av, methodData) { methodData ->
                enabled = methodData.enabled
                logger.debug("$tagName $methodName -> is enabled = $enabled (${methodData})")
            }
            return av2
        } else if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_SKIP};") {
            enabled = false
            methodData.skipMethod = true
            logger.debug("$tagName $className:$methodName -> should be skipped")
        }
        return av
    }

    override fun visitLocalVariable(name: String, descriptor: String, signature: String?, start: Label, end: Label, index: Int) {
        if (enabled && "this" != name && start == labels.first()) {
            logger.debug("$tagName visitLocalVariable($name)")
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
            callback?.invoke(methodData, parameters)
        }
    }
}
