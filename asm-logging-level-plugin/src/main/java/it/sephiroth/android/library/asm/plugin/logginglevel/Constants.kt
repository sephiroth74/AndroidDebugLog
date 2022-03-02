package it.sephiroth.android.library.asm.plugin.logginglevel

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.core.vo.ClassFieldVo
import it.sephiroth.android.library.asm.plugin.core.vo.ClassMethodVo
import org.objectweb.asm.Opcodes

@Suppress("SpellCheckingInspection", "unused")
object Constants {

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BuildConfig.EXTENSION_NAME

    object LoggingLevel {
        const val CLASS_NAME = "it/sephiroth/android/library/asm/runtime/logginglevel/LoggingLevel"
        val SIMPLE_CLASS_NAME = StringUtils.getSimpleClassName(CLASS_NAME)

        // field: MIN_PRIORITY
        val MIN_PRIORITY = ClassFieldVo(CLASS_NAME, "MIN_PRIORITY", "I")

        // static method: isLoggable
        val IS_LOGGABLE = ClassMethodVo(CLASS_NAME, "isLoggable", "(Ljava/lang/String;I)Z", Opcodes.INVOKESTATIC)

        // static method: getMinPriority
        val GET_MIN_PRIORITY = ClassMethodVo(CLASS_NAME, "getMinPriority", "()I", Opcodes.INVOKESTATIC)
    }

    object NullLogger {
        const val CLASS_NAME = "it/sephiroth/android/library/asm/runtime/logginglevel/NoLog"
        val SIMPLE_CLASS_NAME = StringUtils.getSimpleClassName(CLASS_NAME)

        val PRINTLN = ClassMethodVo(CLASS_NAME, "println", "(ILjava/lang/String;Ljava/lang/String;)I", Opcodes.INVOKESTATIC)
    }

    object AndroidLog {
        const val CLASS_NAME = "android/util/Log"
        val IS_LOGGABLE = ClassMethodVo(CLASS_NAME, "isLoggable", "(Ljava/lang/String;I)Z", Opcodes.INVOKESTATIC)
        val PRINTLN = ClassMethodVo(CLASS_NAME, "println", "(ILjava/lang/String;Ljava/lang/String;)I", Opcodes.INVOKESTATIC)
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

}
