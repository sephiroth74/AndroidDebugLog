package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.commons.plugin.AsmPluginExtension

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension : AsmPluginExtension() {

    /**
     * Default log level for the final log output
     */
    var logLevel: AndroidLogLevel = Constants.DEFAULT_LOG_LEVEL

    /**
     * If true by default each method will log also its result and its result timing
     */
    var debugExit: Boolean = Constants.DEFAULT_DEBUG_EXIT

    // var debugEnter: Boolean = Constants.DEFAULT_DEBUG_ENTER

    /**
     * Set the default level of a method parameter logging
     */
    var debugArguments: DebugArguments = Constants.DEFAULT_DEBUG_ARGUMENTS

    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, logLevel=${logLevel}, debugExit=${debugExit}, debugArguments=${debugArguments}, runVariant=${runVariant})"
    }
}
