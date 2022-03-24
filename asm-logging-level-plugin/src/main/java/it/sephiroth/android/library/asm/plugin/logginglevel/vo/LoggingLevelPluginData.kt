package it.sephiroth.android.library.asm.plugin.logginglevel.vo

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import org.gradle.api.tasks.Input

data class LoggingPluginData(
    override val minLogLevel: AndroidLogLevel,
) : ILoggingPluginData

interface ILoggingPluginData : IPluginData {
    @get:Input
    val minLogLevel: AndroidLogLevel
}
