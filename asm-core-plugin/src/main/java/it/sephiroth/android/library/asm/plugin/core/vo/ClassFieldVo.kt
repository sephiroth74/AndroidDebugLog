package it.sephiroth.android.library.asm.plugin.core.vo


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 25.02.22 - 16:23
 */
data class ClassFieldVo(val className: String, val name: String, val descriptor: String) {

    fun matches(name: String?, descriptor: String?): Boolean {
        return this.name == name && this.descriptor == descriptor
    }

    override fun toString(): String {
        return "$className.$name:$descriptor"
    }
}
