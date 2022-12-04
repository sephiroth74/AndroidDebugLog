package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.makeTag
import it.sephiroth.android.library.asm.commons.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.ClassAnnotationData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import org.gradle.api.logging.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
@Suppress("CanBeParameter")
class PreClassVisitor(
    private val classVisitor: ClassVisitor,
    private val classContext: ClassContext,
    private val pluginData: DebugLogPluginParameters
) : ClassVisitor(ASM_VERSION, classVisitor) {

    private val logger = LoggerFactory.getLogger(PreClassVisitor::class.java) as Logger
    private val simpleClassName = StringUtils.getSimpleClassName(classContext.currentClassData.className)
    private val className = classContext.currentClassData.className
    private val tag = makeTag(this)
    private var classData: ClassAnnotationData? = null

    init {
        logger.lifecycle("$tag init(pluginData=$pluginData)")
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_CLASS};") {
            // DebugLogClass for all class methods
            classData = ClassAnnotationData.from(pluginData, className)
            classData!!.enabled = true
            PreAnnotationVisitor(av, classData!!)

        } else if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_SKIP};") {
            classData = ClassAnnotationData.from(pluginData, className)
            classData!!.enabled = false
        }
        return null
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv: MethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == "testMethod") {
            logger.lifecycle("$tag visitMethod($simpleClassName:$name) -> classData:$classData")
            return PreMethodVisitor(name, className, access, name, descriptor, mv, classData, pluginData)
        } else {
            return mv
        }
    }
}
