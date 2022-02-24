package it.sephiroth.android.library.asm.logginglevel

import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.AsmCorePluginExtension

@Suppress("LeakingThis")
abstract class LoggingLevelPluginExtension : AsmCorePluginExtension() {
    /**
     * Minimum log level allowed per project
     * All logs with smaller priority will be removed from the bytecodes
     */
    var minLogLevel: AndroidLogLevel = AndroidLogLevel.VERBOSE

    /**
     * Process or ignore external included libraries
     */
    var includeLibs: Boolean = false


    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, minLogLevel=${minLogLevel}, includeLibs=${includeLibs}, runVariant=${runVariant})"
    }
}
