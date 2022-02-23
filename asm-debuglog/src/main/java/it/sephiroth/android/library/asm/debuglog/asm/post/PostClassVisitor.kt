package it.sephiroth.android.library.asm.debuglog.asm.post

import it.sephiroth.android.library.asm.core.Constants
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.debuglog.asm.vo.MethodParameter
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

class PostClassVisitor(
    cv: ClassWriter,
    private val className: String,
    private val paramsMap: Map<String, Pair<MethodData, List<MethodParameter>>>
) : ClassVisitor(Constants.ASM_VERSION, cv) {

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        val methodUniqueKey = MethodData.generateUniqueKey(name, descriptor)
        val methodDataPair = paramsMap[methodUniqueKey]

        if (null != methodDataPair) {
            return PostMethodVisitor(className, methodDataPair.second, methodDataPair.first, access, descriptor, mv)
        }
        return mv
    }
}
