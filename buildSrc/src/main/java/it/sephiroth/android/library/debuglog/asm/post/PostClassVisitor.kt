package it.sephiroth.android.library.debuglog.asm.post

import it.sephiroth.android.library.debuglog.Constants
import it.sephiroth.android.library.debuglog.DebugLogPlugin
import it.sephiroth.android.library.debuglog.asm.ASMClassWriter
import it.sephiroth.android.library.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.debuglog.asm.vo.MethodParameter
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory

class PostClassVisitor(
    cv: ASMClassWriter,
    private val className: String,
    private val superName: String,
    private val paramsMap: Map<String, Pair<MethodData, List<MethodParameter>>>
) : ClassVisitor(Constants.ASM_VERSION, cv) {

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        val methodUniqueKey = MethodData.generateUniqueKey(name, descriptor)
        val methodDataPair = paramsMap[methodUniqueKey]

        if (null != methodDataPair) {
            logger.lifecycle("methodData: $methodDataPair")
            return PostMethodVisitor(className, methodDataPair.second, methodDataPair.first, access, descriptor, mv)
        }
        return mv
    }


    companion object {
        private const val TAG = DebugLogPlugin.TAG
        val logger: Logger = LoggerFactory.getLogger(PostClassVisitor::class.java) as Logger
    }
}