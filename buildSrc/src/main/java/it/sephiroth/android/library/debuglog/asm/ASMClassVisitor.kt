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

    companion object {
        private const val TAG = DebugLogPlugin.TAG
    }

    private val logger: Logger = LoggerFactory.getLogger(ASMClassVisitor::class.java) as Logger

    val methodsParametersMap = hashMapOf<String, Pair<MethodData, List<MethodParameter>>>()

    var enabled = false
        private set

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        logger.lifecycle("[$TAG] {}::visitMethod(name={}, descriptor={}, signature={})", className, name, descriptor, signature)
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions) ?: return null

        val methodData = MethodData(name, descriptor, pluginData).apply {
            className = this@ASMClassVisitor.className
            simpleClassName = StringUtils.getSimpleClassName(this@ASMClassVisitor.className)
        }

        val mv2 = ASMMethodVisitor(name, className, access, descriptor, mv, methodData, object : ASMMethodVisitor.Callback {
            override fun accept(methodData: MethodData, params: List<MethodParameter>) {
                this@ASMClassVisitor.enabled = true
                this@ASMClassVisitor.methodsParametersMap[methodData.uniqueKey] = Pair(methodData, params)

            }
        })
        return mv2
    }
}
