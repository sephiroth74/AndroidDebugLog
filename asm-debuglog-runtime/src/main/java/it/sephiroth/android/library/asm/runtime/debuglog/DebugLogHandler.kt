package it.sephiroth.android.library.asm.runtime.debuglog

open class DebugLogHandler {

    open fun isLoggable(tag: String?, priority: Int): Boolean = true

    open fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {}

    open fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {}
}
