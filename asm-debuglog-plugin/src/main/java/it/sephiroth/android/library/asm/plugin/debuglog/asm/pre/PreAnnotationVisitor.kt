package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.plugin.core.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import org.objectweb.asm.AnnotationVisitor

/**
 * @author Alessandro Crugnola on 18.11.21 - 13:56
 */
class PreAnnotationVisitor(
    av: AnnotationVisitor?,
    private val methodData: MethodData,
    private val callback: Callback?
) : AnnotationVisitor(Constants.ASM_VERSION, av) {

    override fun visit(name: String, value: Any) {
        when (name) {
            "debugResult" -> methodData.debugResult = value as Boolean
            "logLevel" -> methodData.logLevel = value as Int
            "debugArguments" -> methodData.debugArguments = value as Int
            "enabled" -> methodData.enabled = value as Boolean
            "tag" -> {
                val tag = value as String
                if (tag.isNotBlank()) methodData.tag = tag
            }
        }
        super.visit(name, value)
    }

    override fun visitEnd() {
        super.visitEnd()
        callback?.accept(methodData)
    }

    interface Callback {
        fun accept(methodData: MethodData)
    }
}
