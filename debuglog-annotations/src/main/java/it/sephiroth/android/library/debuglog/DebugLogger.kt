package it.sephiroth.android.library.debuglog

import android.util.Log

object DebugLogger {
    @JvmField
    val nullLogger = DebugLogHandler()

    @JvmField
    val defaultLogger: DebugLogHandler? = object : DebugLogHandler() {
        override fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {
            // ⇢
            Log.println(priority, tag, String.format("\u21E2 %s[%s]", methodName, params))
        }

        override fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {
            // ⇠
            if (null != result) {
                Log.println(priority, tag, String.format("\u21E0 %s[%sms] = %s", methodName, costedMillis, result))
            } else {
                Log.println(priority, tag, String.format("\u21E0 %s[%sms]", methodName, costedMillis))
            }
        }
    }

    @JvmField
    var DEFAULT_IMPL = defaultLogger

    /**
     * Install a new logger
     */
    fun installLog(loggerHandler: DebugLogHandler?) {
        DEFAULT_IMPL = loggerHandler
    }

}
