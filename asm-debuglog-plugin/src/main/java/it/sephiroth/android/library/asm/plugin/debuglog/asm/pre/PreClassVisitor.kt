package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
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
class PreClassVisitor(
    visitor: ClassVisitor?,
    private val classContext: ClassContext,
    private val pluginData: DebugLogPluginParameters,
    private val callback: ((String, HashMap<String, Pair<MethodData, List<MethodParameter>>>) -> Unit)? = null,
) : ClassVisitor(ASM_VERSION, visitor) {

    private val logger = LoggerFactory.getLogger(PreClassVisitor::class.java) as Logger
    private val simpleClassName = StringUtils.getSimpleClassName(classContext.currentClassData.className)
    private val className = classContext.currentClassData.className
    private val tagName = Constants.makeTag(this)

    private var classMethodData: MethodData? = null
    val methodsParametersMap = hashMapOf<String, Pair<MethodData, List<MethodParameter>>>()
    var requireSecondPass = false

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        logger.debug("$tagName visitAnnotation($className, $descriptor)")
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_CLASS};") {
            classMethodData = MethodData("", "", simpleClassName).apply { copyFrom(pluginData) }
            return PreAnnotationVisitor(av, classMethodData!!)
        }
        return av
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        logger.lifecycle("$tagName [pre] visitMethod($name)")

        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        val methodData = MethodData(name, descriptor, simpleClassName).apply {
            copyFrom(pluginData)
            classMethodData?.let { copyFrom(it) }
        }

        return PreMethodVisitor(
            name,
            className,
            access,
            descriptor,
            mv,
            methodData
        ) { methodData1, params ->
            requireSecondPass = true
            methodsParametersMap[methodData1.uniqueKey] = Pair(methodData1, params)
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        if (methodsParametersMap.isNotEmpty()) {
            callback?.invoke(className, methodsParametersMap)
        }
    }
}
