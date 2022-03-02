package it.sephiroth.android.library.asm.runtime.debuglog

import android.util.Log

object DebugLogger {
    @Suppress("unused")
    @JvmField
    val nullLogger = DebugLogHandler()

    @JvmField
    val defaultLogger: DebugLogHandler = DefaultDebugLogHandler()

    @JvmStatic
    var instance = defaultLogger
        private set

    /**
     * Install a new logger
     */
    @Suppress("unused")
    fun installLog(loggerHandler: DebugLogHandler) {
        instance = loggerHandler
    }

    open class DefaultDebugLogHandler : DebugLogHandler() {
        override fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {
            // ⇢
            if (isLoggable(tag, priority)) {
                Log.println(priority, tag, "\u21E2 $methodName[$params]")
            }
        }

        override fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {
            // ⇠
            if (isLoggable(tag, priority)) {
                if (null != result) {
                    Log.println(priority, tag, "\u21E0 $methodName[${costedMillis}ms] = $result")
                } else {
                    Log.println(priority, tag, "\u21E0 $methodName[${costedMillis}ms]")
                }
            }
        }
    }
}
