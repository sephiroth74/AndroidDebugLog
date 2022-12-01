package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import org.gradle.api.logging.Logger
import org.objectweb.asm.AnnotationVisitor
import org.slf4j.LoggerFactory

/**
 * @author Alessandro Crugnola on 18.11.21 - 13:56
 */
class PreAnnotationVisitor(
    av: AnnotationVisitor?,
    private val methodData: MethodData,
    private val callback: ((MethodData) -> Unit)? = null,
) : AnnotationVisitor(ASM_VERSION, av) {
    private val logger: Logger = LoggerFactory.getLogger(PreAnnotationVisitor::class.java) as Logger
    private val tagName = makeTag(this)

    override fun visit(name: String, value: Any) {
        logger.debug("$tagName visiting($name, $value)")
        when (name) {
            "debugExit" -> methodData.debugExit = value as Boolean
            "debugEnter" -> methodData.debugEnter = value as Boolean
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
        callback?.invoke(methodData)
    }

}
