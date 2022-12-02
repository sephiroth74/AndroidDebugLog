package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import com.android.build.api.instrumentation.InstrumentationParameters
import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.mapProperty
import javax.inject.Inject

abstract class DebugLogPluginParameters : InstrumentationParameters {

    @Inject
    abstract fun getObjectFactory(): ObjectFactory

    @get:Input
    abstract val logLevel: Property<AndroidLogLevel>

    @get:Input
    abstract val debugExit: Property<Boolean>

    @get:Input
    abstract val debugEnter: Property<Boolean>

    @get:Input
    abstract val debugArguments: Property<DebugArguments>

    @get:Input
    abstract val buildType: Property<String>

    @get:Input
    abstract val variantName: Property<String>

}
