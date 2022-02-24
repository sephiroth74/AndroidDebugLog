package it.sephiroth.android.library.asm.logginglevel

import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.utils.StringUtils
import org.objectweb.asm.Opcodes

@Suppress("SpellCheckingInspection", "unused")
object Constants {

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BuildConfig.EXTENSION_NAME

    object NullLogger {
        // from asm-common
        const val CLASS_NAME = "it/sephiroth/android/library/asm/commons/logging/NoLog"

        val SIMPLE_CLASS_NAME = StringUtils.getSimpleClassName(CLASS_NAME)

        val IS_LOGGABLE = MethodVo("isLoggable", "(II)Z", Opcodes.INVOKESTATIC)

        val IS_LOGGABLE_TAG = MethodVo("isLoggable", "(Ljava/lang/String;II)Z", Opcodes.INVOKESTATIC)
        val PRINTLN_MIN_LEVEL = MethodVo("println", "(ILjava/lang/String;Ljava/lang/String;I)I", Opcodes.INVOKESTATIC)
        val PRINTLN = MethodVo("println", "(ILjava/lang/String;Ljava/lang/String;)I", Opcodes.INVOKESTATIC)
    }

    object TimberTree {
        const val CLASS_NAME = "timber/log/Timber\$Tree"
        val IS_LOGGABLE = MethodVo("isLoggable", "(I)Z", Opcodes.INVOKEVIRTUAL)
    }


    object AndroidLog {
        const val CLASS_NAME = "android/util/Log"
        val IS_LOGGABLE = MethodVo("isLoggable", "(Ljava/lang/String;I)Z", Opcodes.INVOKESTATIC)
        val PRINTLN = MethodVo("println", "(ILjava/lang/String;Ljava/lang/String;)I", Opcodes.INVOKESTATIC)
    }

    enum class AndroidLogMethods(
        val level: AndroidLogLevel,
        val methodName: String,
        val descriptors: Array<String>
    ) {
        Verbose(
            AndroidLogLevel.VERBOSE,
            "v",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        ),
        Debug(
            AndroidLogLevel.DEBUG,
            "d",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        ),
        Info(
            AndroidLogLevel.INFO,
            "i",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        ),
        Warn(
            AndroidLogLevel.WARN,
            "w",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I",
                "(Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        ),
        Error(
            AndroidLogLevel.ERROR,
            "e",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        ),
        Assert(
            AndroidLogLevel.ASSERT,
            "wtf",
            arrayOf(
                "(Ljava/lang/String;Ljava/lang/String;)I",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I",
                "(Ljava/lang/String;Ljava/lang/Throwable;)I"
            )
        );

        companion object {
            fun contains(methodName: String?, descriptor: String?): AndroidLogMethods? {
                return values().firstOrNull { it.methodName == methodName && descriptor in it.descriptors }
            }

            fun isIndirectMethod(methodName: String?, descriptor: String?): Boolean {
                return values().any { it.methodName == methodName && descriptor in it.descriptors }
            }
        }
    }

    data class MethodVo(val methodName: String, val descriptor: String, val opcode: Int) {

        fun matches(methodName: String?, descriptor: String?, opcode: Int): Boolean {
            return this.methodName == methodName && this.descriptor == descriptor && this.opcode == opcode
        }

        override fun toString(): String {
            return "$methodName$descriptor"
        }
    }
}
