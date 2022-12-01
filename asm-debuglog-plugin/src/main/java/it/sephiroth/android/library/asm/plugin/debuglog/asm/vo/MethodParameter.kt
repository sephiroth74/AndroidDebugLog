package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

data class MethodParameter(val name: String, val descriptor: String, val index: Int) : java.io.Serializable {
    override fun toString(): String {
        return "MethodParameter(name='$name', descriptor='$descriptor', index=$index)"
    }
}
