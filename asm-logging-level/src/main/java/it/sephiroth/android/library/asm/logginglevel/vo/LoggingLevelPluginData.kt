package it.sephiroth.android.library.asm.logginglevel.vo

import org.gradle.api.tasks.Input

data class LoggingPluginData(
    override val minLogLevel: it.sephiroth.android.library.asm.core.AndroidLogLevel,
) : ILoggingPluginData

interface ILoggingPluginData : it.sephiroth.android.library.asm.core.vo.IPluginData {
    @get:Input
    val minLogLevel: it.sephiroth.android.library.asm.core.AndroidLogLevel
}
