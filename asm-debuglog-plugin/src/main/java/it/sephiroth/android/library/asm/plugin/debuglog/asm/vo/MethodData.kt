package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import it.sephiroth.android.library.asm.plugin.debuglog.Constants

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
data class MethodData(
    val name: String,
    val descriptor: String,
    val simpleClassName: String
) {
    var debugExit: Boolean = false
    var debugEnter: Boolean = true
    var logLevel: Int = Constants.DEFAULT_LOG_LEVEL.value
    var debugArguments: Int = Constants.DEFAULT_DEBUG_ARGUMENTS.value
    var tag: String? = null
    var skipMethod: Boolean = false

    var enabled: Boolean = true
        get() = field && !skipMethod && (debugEnter || debugExit)

    val uniqueKey = generateUniqueKey(name, descriptor)

    val finalTag: String
        get() = tag?.takeIf { it.isNotBlank() } ?: simpleClassName

    fun copyFrom(input: DebugLogPluginParameters) {
        debugExit = input.debugExit.get()
        debugEnter = input.debugEnter.get()
        logLevel = input.logLevel.get().value
        debugArguments = input.debugArguments.get().value
    }

    fun copyFrom(input: MethodData) {
        debugExit = input.debugExit
        debugEnter = input.debugEnter
        logLevel = input.logLevel
        debugArguments = input.debugArguments
        enabled = input.enabled
        skipMethod = input.skipMethod
        tag = input.tag
    }

    override fun toString(): String {
        return "MethodData(name='$name', simpleClassName='$simpleClassName', debugExit=$debugExit, debugEnter=$debugEnter, logLevel=$logLevel, debugArguments=$debugArguments, tag=$tag, skipMethod=$skipMethod, enabled=$enabled)"
    }

    companion object {
        fun generateUniqueKey(name: String, descriptor: String) = name + descriptor
    }
}
