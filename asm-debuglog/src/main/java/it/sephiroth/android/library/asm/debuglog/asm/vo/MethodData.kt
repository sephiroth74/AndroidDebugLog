package it.sephiroth.android.library.asm.debuglog.asm.vo

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
    var debugResult: Boolean = false
    var logLevel: Int = it.sephiroth.android.library.asm.debuglog.Constants.DEFAULT_LOG_LEVEL.value
    var debugArguments: Int = it.sephiroth.android.library.asm.debuglog.Constants.DEFAULT_DEBUG_ARGUMENTS.value
    var enabled: Boolean = true
    var tag: String? = null

    val uniqueKey = generateUniqueKey(name, descriptor)

    val finalTag: String
        get() = tag?.takeIf { it.isNotBlank() } ?: simpleClassName

    fun copyFrom(input: IDebugLogPluginData) {
        debugResult = input.debugResult
        logLevel = input.logLevel.value
        debugArguments = input.debugArguments.value
    }

    fun copyFrom(input: MethodData) {
        debugResult = input.debugResult
        logLevel = input.logLevel
        debugArguments = input.debugArguments
        enabled = input.enabled
        tag = input.tag
    }

    companion object {
        fun generateUniqueKey(name: String, descriptor: String) = name + descriptor
    }
}
