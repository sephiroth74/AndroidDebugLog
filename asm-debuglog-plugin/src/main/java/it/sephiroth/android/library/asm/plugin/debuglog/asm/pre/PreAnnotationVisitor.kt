package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.IAnnotationData
import org.gradle.api.logging.Logger
import org.objectweb.asm.AnnotationVisitor
import org.slf4j.LoggerFactory

/**
 * @author Alessandro Crugnola on 18.11.21 - 13:56
 */
class PreAnnotationVisitor(
    av: AnnotationVisitor?,
    private val annotationData: IAnnotationData,
    private val callback: (() -> Unit)? = null
) : AnnotationVisitor(ASM_VERSION, av) {
    private val logger: Logger = LoggerFactory.getLogger(PreAnnotationVisitor::class.java) as Logger
    private val tag = makeTag(this)

    override fun visit(name: String, value: Any) {
        logger.debug("$tag visit($name, $value)")
        when (name) {
            "debugExit" -> annotationData.debugExit = value as Boolean
            "debugEnter" -> annotationData.debugEnter = value as Boolean
            "logLevel" -> annotationData.logLevel = value as Int
            "debugArguments" -> annotationData.debugArguments = value as Int
            "tag" -> {
                val tag = value as String
                if (tag.isNotBlank()) annotationData.tag = tag
            }
        }
        super.visit(name, value)
    }

    override fun visitEnd() {
        super.visitEnd()
        callback?.invoke()
    }
}
