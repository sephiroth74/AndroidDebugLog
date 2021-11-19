package it.sephiroth.android.library.debuglog.asm.vo

import it.sephiroth.android.library.debuglog.Constants

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
data class MethodData(val name: String, val descriptor: String, val className: String, val simpleClassName: String) {
    var debugResult: Boolean = false
    var logLevel: Int = Constants.DEFAULT_LOG_LEVEL.value
    var debugArguments: Int = Constants.DEFAULT_DEBUG_ARGUMENTS.value
    var enabled: Boolean = true

    val uniqueKey = generateUniqueKey(name, descriptor)

    fun copyFrom(input: IPluginData) {
        debugResult = input.debugResult
        logLevel = input.logLevel.value
        debugArguments = input.debugArguments.value
    }

    fun copyFrom(input: MethodData) {
        debugResult = input.debugResult
        logLevel = input.logLevel
        debugArguments = input.debugArguments
        enabled = input.enabled
    }

    companion object {
        fun generateUniqueKey(name: String, descriptor: String) = name + descriptor
    }
}
