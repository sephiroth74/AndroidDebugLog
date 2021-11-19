package it.sephiroth.android.library.debuglog

import android.util.Log

object DebugLogger {
    @Suppress("unused")
    @JvmField
    val nullLogger = DebugLogHandler()

    @JvmField
    val defaultLogger: DebugLogHandler = object : DebugLogHandler() {
        override fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {
            // ⇢
            Log.println(priority, tag, "\u21E2 $methodName[$params]")
        }

        override fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {
            // ⇠
            if (null != result) {
                Log.println(priority, tag, "\u21E0 $methodName[${costedMillis}ms] = $result")
            } else {
                Log.println(priority, tag, "\u21E0 $methodName[${costedMillis}ms]")
            }
        }
    }

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

}
