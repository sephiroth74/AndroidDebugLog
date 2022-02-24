package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.AsmCorePluginExtension

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension : AsmCorePluginExtension() {

    /**
     * Default log level for the final log output
     */
    var logLevel: AndroidLogLevel = Constants.DEFAULT_LOG_LEVEL

    /**
     * If true by default each method will log also its result and its result timing
     */
    var debugResult: Boolean = Constants.DEFAULT_DEBUG_RESULT

    /**
     * Set the default level of a method parameter logging
     */
    var debugArguments: DebugArguments = Constants.DEFAULT_DEBUG_ARGUMENTS


    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, logLevel=${logLevel}, debugResult=${debugResult}, debugArguments=${debugArguments}, runVariant=${runVariant})"
    }
}
