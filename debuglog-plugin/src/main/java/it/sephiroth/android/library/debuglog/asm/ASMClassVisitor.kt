package it.sephiroth.android.library.debuglog.asm

import it.sephiroth.android.library.debuglog.Constants
import it.sephiroth.android.library.debuglog.DebugLogPlugin
import it.sephiroth.android.library.debuglog.asm.vo.IPluginData
import it.sephiroth.android.library.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.debuglog.asm.vo.MethodParameter
import it.sephiroth.android.library.debuglog.utils.StringUtils
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
class ASMClassVisitor(
        cv: ASMClassWriter,
        private val className: String,
        private val superName: String,
        private val pluginData: IPluginData
) : ClassVisitor(Constants.ASM_VERSION, cv) {

    private val logger: Logger = LoggerFactory.getLogger(ASMClassVisitor::class.java) as Logger
    private var classMethodData: MethodData? = null
    private val simpleClassName = StringUtils.getSimpleClassName(className)

    val methodsParametersMap = hashMapOf<String, Pair<MethodData, List<MethodParameter>>>()

    var enabled = false
        private set

    init {
        logger.debug("[$TAG] visiting class $className")
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.debug("{} visitAnnotation({})", className, descriptor)
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_CLASS};") {
            classMethodData = MethodData("", "", className, simpleClassName).apply { copyFrom(pluginData) }
            val av2 = ASMAnnotationVisitor(av, classMethodData!!, null)
            return av2
        }
        return av
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions) ?: return null

        val methodData = MethodData(name, descriptor, className, simpleClassName).apply { copyFrom(pluginData) }

        val mv2 = ASMMethodVisitor(name, className, access, descriptor, mv, methodData, classMethodData, object : ASMMethodVisitor.Callback {
            override fun accept(methodData: MethodData, params: List<MethodParameter>) {
                logger.lifecycle("[$TAG] transformation enabled for {}::{}", className, methodData.name)
                this@ASMClassVisitor.enabled = true
                this@ASMClassVisitor.methodsParametersMap[methodData.uniqueKey] = Pair(methodData, params)

            }
        })
        return mv2
    }

    companion object {
        private const val TAG = "${DebugLogPlugin.TAG}:ASMClassVisitor"
    }
}
