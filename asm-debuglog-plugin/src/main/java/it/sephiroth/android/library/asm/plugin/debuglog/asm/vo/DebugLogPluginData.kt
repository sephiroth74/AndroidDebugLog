package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import org.gradle.api.tasks.Input

data class DebugLogPluginData(
    override val logLevel: AndroidLogLevel,
    override val debugResult: Boolean,
    override val debugArguments: DebugArguments
) : IDebugLogPluginData {
    constructor(input: IDebugLogPluginData) : this(input.logLevel, input.debugResult, input.debugArguments)
}

interface IDebugLogPluginData : IPluginData {
    @get:Input
    val logLevel: AndroidLogLevel

    @get:Input
    val debugResult: Boolean

    @get:Input
    val debugArguments: DebugArguments
}
