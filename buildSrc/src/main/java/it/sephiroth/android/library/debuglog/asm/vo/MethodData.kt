package it.sephiroth.android.library.debuglog.asm.vo

import it.sephiroth.android.library.debuglog.Constants

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
data class MethodData(val name: String, val descriptor: String) {
    var debugResult: Boolean = false
    var logLevel: Int = Constants.DEFAULT_LOG_LEVEL.value
    var debugArguments: Int = Constants.DEFAULT_DEBUG_ARGUMENTS.value
    var enabled: Boolean = true

    lateinit var className: String
    lateinit var simpleClassName: String

    val uniqueKey = generateUniqueKey(name, descriptor)

    constructor(name: String, descriptor: String, input: IPluginData) : this(name, descriptor) {
        debugResult = input.debugResult
        logLevel = input.logLevel.value
        debugArguments = input.debugArguments.value
    }

    companion object {
        fun generateUniqueKey(name: String, descriptor: String) = name + descriptor
    }
}
