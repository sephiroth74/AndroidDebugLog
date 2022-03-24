package it.sephiroth.android.library.asm.plugin.core.vo


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 25.02.22 - 16:23
 */
data class ClassMethodVo(val className: String, val methodName: String, val descriptor: String, val opcode: Int) {

    fun matches(methodName: String?, descriptor: String?, opcode: Int): Boolean {
        return this.methodName == methodName && this.descriptor == descriptor && this.opcode == opcode
    }

    fun matches(methodName: String?, descriptor: String?): Boolean {
        return this.methodName == methodName && this.descriptor == descriptor
    }

    override fun toString(): String {
        return "$className.$methodName$descriptor"
    }
}
