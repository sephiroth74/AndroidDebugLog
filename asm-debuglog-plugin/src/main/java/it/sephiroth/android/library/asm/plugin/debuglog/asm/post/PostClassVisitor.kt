package it.sephiroth.android.library.asm.plugin.debuglog.asm.post

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory

class PostClassVisitor(
    private val classContext: ClassContext,
    nextClassVisitor: ClassVisitor,
    private val paramsMap: Map<String, Pair<MethodData, List<MethodParameter>>>
) : ClassVisitor(ASM_VERSION, nextClassVisitor) {

    private val className: String = classContext.currentClassData.className
    private val logger: Logger = LoggerFactory.getLogger(PostClassVisitor::class.java) as Logger
    private val tagName = Constants.makeTag(this)

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {

        logger.lifecycle("[$tagName] visitMethod($name)")

        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        val methodUniqueKey = MethodData.generateUniqueKey(name, descriptor)
        val methodDataPair = paramsMap[methodUniqueKey]

        if (null != methodDataPair) {
            return PostMethodVisitor(classContext, mv, methodDataPair.second, methodDataPair.first, access, descriptor)
        }
        return mv
    }
}
