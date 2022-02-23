package it.sephiroth.android.library.asm.debuglog.asm.vo

import it.sephiroth.android.library.asm.debuglog.DebugArguments
import org.gradle.api.tasks.Input

data class DebugLogPluginData(
    override val logLevel: it.sephiroth.android.library.asm.core.AndroidLogLevel,
    override val debugResult: Boolean,
    override val debugArguments: DebugArguments
) : IDebugLogPluginData {
    constructor(input: IDebugLogPluginData) : this(input.logLevel, input.debugResult, input.debugArguments)
}

interface IDebugLogPluginData : it.sephiroth.android.library.asm.core.vo.IPluginData {
    @get:Input
    val logLevel: it.sephiroth.android.library.asm.core.AndroidLogLevel

    @get:Input
    val debugResult: Boolean

    @get:Input
    val debugArguments: DebugArguments
}
