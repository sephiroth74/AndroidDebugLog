package it.sephiroth.android.library.asm.plugin.debuglog.asm.pre

import it.sephiroth.android.library.asm.plugin.core.AsmClassVisitor
import it.sephiroth.android.library.asm.plugin.core.AsmClassWriter
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import it.sephiroth.android.library.asm.plugin.debuglog.asm.post.PostClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.MethodVisitor

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
class PreClassVisitor(
    cv: AsmClassWriter,
    className: String,
    superName: String,
    pluginData: IPluginData
) : AsmClassVisitor(cv, className, superName, pluginData) {

    private var classMethodData: MethodData? = null

    val methodsParametersMap = hashMapOf<String, Pair<MethodData, List<MethodParameter>>>()
    val tagName = "[${Constants.makeTag(this)}]"

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.debug("$tagName visitAnnotation($className, $descriptor)")
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_CLASS};") {
            classMethodData = MethodData("", "", simpleClassName).apply { copyFrom(pluginData as DebugLogPluginData) }
            return PreAnnotationVisitor(av, classMethodData!!, null)
        }
        return av
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions) ?: return null

        val methodData = MethodData(name, descriptor, simpleClassName).apply {
            copyFrom(pluginData as DebugLogPluginData)
        }

        val mv2 = PreMethodVisitor(name, className, access, descriptor, mv, methodData, classMethodData, object : PreMethodVisitor.Callback {
            override fun accept(methodData: MethodData, params: List<MethodParameter>) {
                logger.info("$tagName second pass required for $className::${methodData.name}")
                requireSecondPass = true
                methodsParametersMap[methodData.uniqueKey] = Pair(methodData, params)

            }
        })
        return mv2
    }

    override fun executeSecondPass(classWriter: AsmClassWriter, classReader: ClassReader) {
        logger.debug("$tagName executing secondPass on $className")

        classReader.accept(
            PostClassVisitor(
                classWriter,
                className,
                methodsParametersMap
            ), ClassReader.EXPAND_FRAMES
        )
    }
}
