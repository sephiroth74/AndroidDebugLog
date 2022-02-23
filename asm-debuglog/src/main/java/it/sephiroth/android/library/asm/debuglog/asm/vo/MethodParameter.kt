package it.sephiroth.android.library.asm.debuglog.asm.vo

data class MethodParameter(val name: String, val descriptor: String, val index: Int) {
    override fun toString(): String {
        return "MethodParameter(name='$name', descriptor='$descriptor', index=$index)"
    }
}
