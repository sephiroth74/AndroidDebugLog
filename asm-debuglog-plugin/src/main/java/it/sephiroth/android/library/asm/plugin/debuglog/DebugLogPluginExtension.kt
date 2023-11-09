package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.commons.plugin.AsmPluginExtension
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class DebugLogPluginExtension : AsmPluginExtension() {

    @Inject
    abstract fun getObjectFactory(): ObjectFactory

    /**
     * Default log level for the final log output
     */
    var logLevel: AndroidLogLevel = Constants.DEFAULT_LOG_LEVEL

    /**
     * If true by default each method will log also its result and its result timing
     */
    var debugExit: Boolean = Constants.DEFAULT_DEBUG_EXIT

    /**
     * Set the default level of a method parameter logging
     */
    var debugArguments: DebugArguments = Constants.DEFAULT_DEBUG_ARGUMENTS

    @get:Input
    var verbose: Property<Boolean> = getObjectFactory().property()

    init {
        verbose.convention(false)
    }

    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, logLevel=${logLevel}, debugExit=${debugExit}, debugArguments=${debugArguments}, runVariant=${runVariant})"
    }
}
