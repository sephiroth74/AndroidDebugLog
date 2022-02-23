package it.sephiroth.android.library.asm.debuglog.asm.pre

import it.sephiroth.android.library.asm.core.AsmClassVisitor
import it.sephiroth.android.library.asm.core.AsmClassWriter
import it.sephiroth.android.library.asm.core.utils.StringUtils
import it.sephiroth.android.library.asm.core.vo.IPluginData
import it.sephiroth.android.library.asm.debuglog.Constants
import it.sephiroth.android.library.asm.debuglog.asm.post.PostClassVisitor
import it.sephiroth.android.library.asm.debuglog.asm.vo.DebugLogPluginData
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodParameter
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

    init {
        logger.debug("[$TAG] visiting class $className")
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        logger.debug("{} visitAnnotation({})", className, descriptor)
        val av = super.visitAnnotation(descriptor, visible)
        if (descriptor == "L${it.sephiroth.android.library.asm.debuglog.Constants.JavaTypes.TYPE_ANNOTATION_DEBUGLOG_CLASS};") {
            classMethodData = MethodData("", "", simpleClassName).apply { copyFrom(pluginData as DebugLogPluginData) }
            return PreAnnotationVisitor(av, classMethodData!!, null)
        }
        return av
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions) ?: return null

        val methodData = MethodData(name, descriptor, simpleClassName).apply { copyFrom(pluginData as DebugLogPluginData) }

        val mv2 = PreMethodVisitor(name, className, access, descriptor, mv, methodData, classMethodData, object : PreMethodVisitor.Callback {
            override fun accept(methodData: MethodData, params: List<MethodParameter>) {
                logger.info("[$TAG] transformation enabled for {}::{}", className, methodData.name)
                enabled = true
                methodsParametersMap[methodData.uniqueKey] = Pair(methodData, params)

            }
        })
        return mv2
    }

    override fun secondPass(classWriter: AsmClassWriter, classReader: ClassReader) {
        logger.debug("[$TAG] executing secondPass for $className")

        classReader.accept(
            PostClassVisitor(
                classWriter,
                className,
                methodsParametersMap
            ), ClassReader.EXPAND_FRAMES
        )
    }


    companion object {
        private const val TAG = "[${Constants.DEBUGLOG_EXTENSION}]|PreClassVisitor"
    }
}
