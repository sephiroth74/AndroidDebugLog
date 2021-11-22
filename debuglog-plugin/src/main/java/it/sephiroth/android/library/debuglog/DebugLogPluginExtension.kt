package it.sephiroth.android.library.debuglog

import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension {

    /**
     * Globally enable/disable the transformation plugin
     */
    abstract val enabled: Property<Boolean>

    /**
     * Default log level for the final log output
     */
    abstract val logLevel: Property<AndroidLogLevel>

    /**
     * If true by default each method will log also its result and its result timing
     */
    abstract val debugResult: Property<Boolean>

    /**
     * Set the default level of a method parameter logging
     */
    abstract val debugArguments: Property<DebugArguments>

    /**
     * Set the name (regexp pattern) on which variant the transformation will be executed
     */
    abstract val runVariant: Property<String>

    init {
        enabled.convention(true)
        logLevel.convention(Constants.DEFAULT_LOG_LEVEL)
        debugResult.convention(Constants.DEFAULT_DEBUG_RESULT)
        debugArguments.convention(Constants.DEFAULT_DEBUG_ARGUMENTS)
        runVariant.convention(".*")
    }
}
