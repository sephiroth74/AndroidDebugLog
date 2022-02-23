package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.AsmCorePluginExtension
import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension : AsmCorePluginExtension() {

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

    override fun toString(): String {
        return "${javaClass.simpleName}(enabled=${enabled.get()}, logLevel=${logLevel.get()}, debugResult=${debugResult.get()}, debugArguments=${debugArguments.get()}, runVariant=${runVariant.get()})"
    }

    init {
        enabled.convention(true)
        logLevel.convention(Constants.DEFAULT_LOG_LEVEL)
        debugResult.convention(Constants.DEFAULT_DEBUG_RESULT)
        debugArguments.convention(Constants.DEFAULT_DEBUG_ARGUMENTS)
        runVariant.convention(".*")
    }

}
