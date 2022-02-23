package it.sephiroth.android.library.asm.logginglevel

import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.AsmCorePluginExtension
import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class LoggingLevelPluginExtension : AsmCorePluginExtension() {

    /**
     * Minimum log level allowed per project
     * All logs with smaller priority will be removed from the bytecodes
     */
    abstract val minLogLevel: Property<AndroidLogLevel>

    /**
     * Set the name (regexp pattern) on which variant the transformation will be executed
     */
    abstract val runVariant: Property<String>

    /**
     * Process or ignore external included libraries
     */
    abstract val includeLibs: Property<Boolean>

    override fun toString(): String {
        return "${javaClass.simpleName}(enabled=${enabled.get()}, minLogLevel=${minLogLevel.get()}, includeLibs=${includeLibs.get()}, runVariant=${runVariant.get()})"
    }

    init {
        enabled.convention(true)
        minLogLevel.convention(Constants.MINIMUM_LOG_LEVEL)
        includeLibs.convention(false)
        runVariant.convention(".*")
    }
}
