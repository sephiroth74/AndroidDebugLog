package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import com.android.build.api.instrumentation.InstrumentationParameters
import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface DebugLogPluginParameters : InstrumentationParameters {
    @get:Input
    val logLevel: Property<AndroidLogLevel>

    @get:Input
    val debugExit: Property<Boolean>

    @get:Input
    val debugEnter: Property<Boolean>

    @get:Input
    val debugArguments: Property<DebugArguments>

    @get:Input
    val buildType: Property<String>

    @get:Input
    val variantName: Property<String>
}
