package it.sephiroth.android.library.asm.core.utils

object StringUtils {
    fun replaceLastPart(originString: String, replacement: String, toReplace: String): String {
        val start = originString.lastIndexOf(replacement)
        val builder = StringBuilder()
        builder.append(originString, 0, start)
        builder.append(toReplace)
        builder.append(originString.substring(start + replacement.length))
        return builder.toString()
    }

    fun getSimpleClassName(fullClassName: String): String {
        return fullClassName.split("/").last()
    }
}
