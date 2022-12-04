package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import com.android.build.api.instrumentation.InstrumentationParameters
import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class DebugLogPluginParameters : InstrumentationParameters {

    override fun toString(): String {
        return "DebugLogPluginParameters(" +
                "logLevel=${logLevel.orNull}, " +
                "debugExit=${debugExit.orNull}, " +
                "debugEnter=${debugEnter.orNull}, " +
                "debugArguments=${debugArguments.orNull}, " +
                "verbose=${verbose.orNull}, " +
                ")"
    }

    @get:Input
    abstract val logLevel: Property<AndroidLogLevel>

    @get:Input
    abstract val debugExit: Property<Boolean>

    @get:Input
    abstract val debugEnter: Property<Boolean>

    @get:Input
    abstract val debugArguments: Property<DebugArguments>

    @get:Input
    abstract val verbose: Property<Boolean>

}
