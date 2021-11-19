package it.sephiroth.android.library.debuglog.asm.pre

import it.sephiroth.android.library.debuglog.Constants
import it.sephiroth.android.library.debuglog.asm.vo.MethodData
import org.objectweb.asm.AnnotationVisitor

/**
 * @author Alessandro Crugnola on 18.11.21 - 13:56
 */
class ASMAnnotationVisitor(av: AnnotationVisitor?,
                           private val methodData: MethodData,
                           private val callback: Callback?) : AnnotationVisitor(Constants.ASM_VERSION, av) {

    override fun visit(name: String, value: Any) {
        if ("debugResult" == name) {
            methodData.debugResult = value as Boolean
        } else if ("logLevel" == name) {
            methodData.logLevel = value as Int
        } else if ("debugArguments" == name) {
            methodData.debugArguments = value as Int
        } else if ("enabled" == name) {
            methodData.enabled = value as Boolean
        }
        super.visit(name, value)
    }

    override fun visitEnd() {
        super.visitEnd()
        callback?.accept(methodData)
    }

    interface Callback {
        fun accept(methodData: MethodData)
    }
}
