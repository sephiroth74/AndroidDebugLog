package it.sephiroth.android.library.asm.runtime.debuglog

open class DebugLogHandler {

    open fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {}

    open fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {}
}
