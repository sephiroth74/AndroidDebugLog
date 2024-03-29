package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.commons.AndroidLogLevel.*
import it.sephiroth.android.library.asm.commons.vo.ClassMethodVo
import org.objectweb.asm.Opcodes

@Suppress("SpellCheckingInspection", "unused")
object Constants {

    // const val DEFAULT_REPLACE_TIMBER = false

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BuildConfig.EXTENSION_NAME

    object ILogger {
        const val CLASS_NAME = "it/sephiroth/android/library/asm/runtime/logging/ILogger"

        private val V = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val V_THROWABLE = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val V_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "v", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        private val D = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val D_THROWABLE = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val D_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "d", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        private val I = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val I_THROWABLE = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val I_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "i", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        private val W = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val W_THROWABLE = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val W_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "w", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        private val E = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val E_THROWABLE = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val E_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "e", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        private val WTF = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val WTF_THROWABLE = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKEINTERFACE)
        private val WTF_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "wtf", "(Ljava/lang/Throwable;)V", Opcodes.INVOKEINTERFACE)

        // replacements
        private val LOG_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/String;[Ljava/lang/Object;II)V", Opcodes.INVOKEINTERFACE)
        private val LOG_THROWABLE_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;II)V", Opcodes.INVOKEINTERFACE)
        private val LOG_THROWABLE_ONLY_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;II)V", Opcodes.INVOKEINTERFACE)

        val GET_TAG = ClassMethodVo(CLASS_NAME, "getTag", "()Ljava/lang/String;", Opcodes.INVOKEINTERFACE)

        fun replace(methodName: String?, descriptor: String?, opcode: Int): Pair<Int, ClassMethodVo>? {
            return when {
                V.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_THROWABLE_ONLY_TAG)

                else -> null
            }
        }
    }

    object Trunk {
        const val CLASS_NAME = "it/sephiroth/android/library/asm/runtime/logging/Trunk"

        // original calls
        private val ONCE = ClassMethodVo(CLASS_NAME, "once", "(IILjava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val ONCE_THROWABLE = ClassMethodVo(CLASS_NAME, "once", "(IILjava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V", Opcodes.INVOKESTATIC)
        private val ONCE_THROWABLE_ONLY = ClassMethodVo(CLASS_NAME, "once", "(IILjava/lang/Throwable;)V", Opcodes.INVOKESTATIC)

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
        internal val LOG_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;I)V", Opcodes.INVOKESTATIC)
        internal val LOG_THROWABLE_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;I)V", Opcodes.INVOKESTATIC)
        internal val LOG_THROWABLE_ONLY_TAG = ClassMethodVo(CLASS_NAME, "log", "(Ljava/lang/Throwable;ILjava/lang/String;I)V", Opcodes.INVOKESTATIC)

        internal val LOG_ONCE_TAG = ClassMethodVo(CLASS_NAME, "logOnce", "(IILjava/lang/String;[Ljava/lang/Object;Ljava/lang/String;I)V", Opcodes.INVOKESTATIC)
        internal val LOG_ONCE_THROWABLE_TAG = ClassMethodVo(CLASS_NAME, "logOnce", "(IILjava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/String;I)V", Opcodes.INVOKESTATIC)
        internal val LOG_ONCE_THROWABLE_ONLY_TAG = ClassMethodVo(CLASS_NAME, "logOnce", "(IILjava/lang/Throwable;Ljava/lang/String;I)V", Opcodes.INVOKESTATIC)

        fun replace(methodName: String?, descriptor: String?, opcode: Int): Pair<Int, ClassMethodVo>? {
            return when {
                ONCE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_ONCE_TAG)
                ONCE_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_ONCE_THROWABLE_TAG)
                ONCE_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_ONCE_THROWABLE_ONLY_TAG)

                V.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(INFO.value, LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(WARN.value, LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }
    }

    object TimberForest {
        private const val CLASS_NAME = "timber/log/Timber\$Forest"

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
                V.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }

    }

    object Timber {
        private const val CLASS_NAME = "timber/log/Timber"

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
                V.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_TAG)
                V_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_THROWABLE_TAG)
                V_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(VERBOSE.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                D.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_TAG)
                D_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_THROWABLE_TAG)
                D_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(DEBUG.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                I.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_TAG)
                I_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_THROWABLE_TAG)
                I_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(INFO.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                W.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_TAG)
                W_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_THROWABLE_TAG)
                W_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(WARN.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                E.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_TAG)
                E_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_THROWABLE_TAG)
                E_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ERROR.value, Trunk.LOG_THROWABLE_ONLY_TAG)

                WTF.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_TAG)
                WTF_THROWABLE.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_THROWABLE_TAG)
                WTF_THROWABLE_ONLY.matches(methodName, descriptor, opcode) -> Pair(ASSERT.value, Trunk.LOG_THROWABLE_ONLY_TAG)
                else -> null
            }
        }

    }
}
