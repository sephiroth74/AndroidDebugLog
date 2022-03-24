package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments
import org.gradle.api.tasks.Input

data class DebugLogPluginData(
    override val logLevel: AndroidLogLevel,
    override val debugExit: Boolean,
    override val debugArguments: DebugArguments
) : IDebugLogPluginData {
    constructor(input: IDebugLogPluginData) : this(input.logLevel, input.debugExit, input.debugArguments)

    override val debugEnter: Boolean
        get() = true
}

interface IDebugLogPluginData : IPluginData {
    @get:Input
    val logLevel: AndroidLogLevel

    @get:Input
    val debugExit: Boolean

    @get:Input
    val debugEnter: Boolean

    @get:Input
    val debugArguments: DebugArguments
}
