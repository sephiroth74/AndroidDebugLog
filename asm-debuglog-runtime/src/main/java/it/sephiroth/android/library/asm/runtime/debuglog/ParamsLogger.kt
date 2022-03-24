package it.sephiroth.android.library.asm.runtime.debuglog

import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger.instance
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments

@Suppress("unused")
class ParamsLogger(
    private val tag: String,
    private val methodName: String,
    private val debugArguments: Int
) {
    private var paramIndex = 0
    private val paramsList = StringBuilder()
    private val divider = ", "
    private val printArguments = true

    fun append(name: String?): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(name)
        return this
    }

    fun append(name: String?, value: Int): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Boolean): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Short): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Byte): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Char): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Long): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Double): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Float): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
        return this
    }

    fun append(name: String?, value: Any?): ParamsLogger {
        if (!printArguments) return this
        if (paramIndex++ != 0) {
            paramsList.append(divider)
        }
        if (null != value) {
            if (debugArguments == DebugArguments.FULL) {
                if (value.javaClass.isArray) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, arrayToString(value)))
                } else {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
                }
            } else if (debugArguments == DebugArguments.SHORT) {
                if (value.javaClass.isArray) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, arrayToHashCode(value)))
                } else if (value is String || value is Enum<*>) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, value))
                } else {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, objectToHashCode(value)))
                }
            }
        } else {
            paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, "null"))
        }
        return this
    }

    fun print(level: Int) {
        instance.logEnter(level, tag, methodName, paramsList.toString())
    }

    companion object {
        @JvmStatic
        fun objectToHashCode(value: Any?): String {
            return if (value == null) "null" else value::class.java.name + "#" + System.identityHashCode(value)
        }

        @JvmStatic
        fun arrayToHashCode(value: Any?): String {
            return if (value == null) "null" else value::class.java.name + "[size=" + java.lang.reflect.Array.getLength(value) + "]"
        }

        @JvmStatic
        internal fun arrayToString(value: Any): String {
            return when (value) {
                is Array<*> -> value.contentDeepToString()
                is ByteArray -> value.contentToString()
                is ShortArray -> value.contentToString()
                is IntArray -> value.contentToString()
                is LongArray -> value.contentToString()
                is FloatArray -> value.contentToString()
                is DoubleArray -> value.contentToString()
                is CharArray -> value.contentToString()
                is BooleanArray -> value.contentToString()

                // Experimental types
//            is UByteArray -> value.contentToString()
//            is UShortArray -> value.contentToString()
//            is UIntArray -> value.contentToString()
//            is ULongArray -> value.contentToString()
                else -> value.toString()
            }
        }

        const val PARAMETER_PRINT_FORMAT = "%s=\"%s\""
    }
}
