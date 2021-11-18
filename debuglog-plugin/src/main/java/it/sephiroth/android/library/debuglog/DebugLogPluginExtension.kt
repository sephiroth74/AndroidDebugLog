package it.sephiroth.android.library.debuglog

import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension {

    abstract val enabled: Property<Boolean>

    abstract val logLevel: Property<AndroidLogLevel>

    abstract val debugResult: Property<Boolean>

    abstract val debugArguments: Property<DebugArguments>

    abstract val runVariant: Property<RunVariant>

    init {
        enabled.convention(true)
        logLevel.convention(Constants.DEFAULT_LOG_LEVEL)
        debugResult.convention(Constants.DEFAULT_DEBUG_RESULT)
        debugArguments.convention(Constants.DEFAULT_DEBUG_ARGUMENTS)
        runVariant.convention(RunVariant.Always)
    }
}
