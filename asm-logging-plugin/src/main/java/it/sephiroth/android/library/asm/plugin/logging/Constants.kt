package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.vo.ClassMethodVo
import org.objectweb.asm.Opcodes

@Suppress("SpellCheckingInspection", "unused")
object Constants {

    // const val DEFAULT_REPLACE_TIMBER = false

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BuildConfig.EXTENSION_NAME

    object Trunk {
        const val CLASS_NAME = "it/sephiroth/android/library/asm/runtime/logging/Trunk"

        // original calls
        private val V = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val V_THROWABLE = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val V_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val D = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val D_THROWABLE = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val D_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val I = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val I_THROWABLE = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val I_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val W = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val W_THROWABLE = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val W_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val E = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val E_THROWABLE = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val E_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val WTF = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val WTF_THROWABLE = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val WTF_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        // replacements
        internal val LOG_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;)V", Opcodes.INVOKESTATIC)
        internal val LOG_THROWABLE_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;)V", Opcodes.INVOKESTATIC)
        internal val LOG_THROWABLE_ONLY_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;ILjava/lang/String;)V", Opcodes.INVOKESTATIC)


        fun replace(methodName: String?, descriptor: String?, opcode: Int): Pair<Int, ClassMethodVo>? {
            return when {
                V.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }
    }

    object TimberForest {
        const val CLASS_NAME = "timber/log/Timber\$Forest"

        // original calls
        val V = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val V_THROWABLE = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val V_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)

        private val D = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val D_THROWABLE = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val D_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)

        private val I = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val I_THROWABLE = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val I_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)

        private val W = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val W_THROWABLE = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val W_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)

        private val E = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val E_THROWABLE = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val E_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)

        private val WTF = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val WTF_THROWABLE = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEVIRTUAL)
        private val WTF_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEVIRTUAL)


        fun replace(methodName: String?, descriptor: String?, opcode: Int): Pair<Int, ClassMethodVo>? {
            return when {
                V.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }

    }

    object Timber {
        const val CLASS_NAME = "timber/log/Timber"

        // original calls
        private val V = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val V_THROWABLE = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val V_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val D = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val D_THROWABLE = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val D_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val I = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val I_THROWABLE = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val I_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val W = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val W_THROWABLE = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val W_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val E = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val E_THROWABLE = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val E_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

        private val WTF = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val WTF_THROWABLE = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val WTF_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;)V", Opcodes.INVOKESTATIC)


        fun replace(methodName: String?, descriptor: String?, opcode: Int): Pair<Int, ClassMethodVo>? {
            return when {
                V.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.VERBOSE.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.DEBUG.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.INFO.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.WARN.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ERROR.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(AndroidLogLevel.ASSERT.value, Trunk.LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }

    }
}
