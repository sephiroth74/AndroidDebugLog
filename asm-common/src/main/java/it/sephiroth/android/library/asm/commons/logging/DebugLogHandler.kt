package it.sephiroth.android.library.asm.commons.logging

open class DebugLogHandler {

    open fun logEnter(priority: Int, tag: String, methodName: String, params: String?) {}

    open fun logExit(priority: Int, tag: String, methodName: String, costedMillis: Long, result: String?) {}
}
