package it.sephiroth.android.library.debuglog.asm.vo

import it.sephiroth.android.library.debuglog.AndroidLogLevel
import it.sephiroth.android.library.debuglog.DebugArguments
import org.gradle.api.tasks.Input
import java.io.Serializable

data class PluginData(override val logLevel: AndroidLogLevel, override val debugResult: Boolean, override val debugArguments: DebugArguments) : IPluginData {
    constructor(input: IPluginData) : this(input.logLevel, input.debugResult, input.debugArguments)
}

interface IPluginData : Serializable {
    @get:Input
    val logLevel: AndroidLogLevel

    @get:Input
    val debugResult: Boolean

    @get:Input
    val debugArguments: DebugArguments
}
