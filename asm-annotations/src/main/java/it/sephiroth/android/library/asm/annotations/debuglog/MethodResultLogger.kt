package it.sephiroth.android.library.asm.annotations.debuglog

import it.sephiroth.android.library.asm.annotations.debuglog.ParamsLogger.Companion.arrayToHashCode
import it.sephiroth.android.library.asm.annotations.debuglog.ParamsLogger.Companion.objectToHashCode

@Suppress("unused", "UNUSED_PARAMETER")
object MethodResultLogger {
    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Byte) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Char) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Short) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Int) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Boolean) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Long) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Float) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Double) {
        DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
    }

    @JvmStatic
    fun print(level: Int, printArguments: Int, className: String?, methodName: String?, costedMillis: Long, returnVal: Any?) {
        if (printArguments == DebugArguments.NONE) {
            DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, null)
        } else {
            if (returnVal != null && returnVal.javaClass.isArray) { // array
                if (printArguments == DebugArguments.FULL) {
                    DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, ParamsLogger.arrayToString(returnVal))
                } else {
                    DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, arrayToHashCode(returnVal))
                }
            } else if (returnVal is String || returnVal is Enum<*>) { // String, Enum
                DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
            } else { // everything else
                if (printArguments == DebugArguments.FULL || returnVal == null) {
                    DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, returnVal.toString() + "")
                } else {
                    DebugLogger.instance.logExit(level, className!!, methodName!!, costedMillis, objectToHashCode(returnVal) + "")
                }
            }
        }
    }
}
