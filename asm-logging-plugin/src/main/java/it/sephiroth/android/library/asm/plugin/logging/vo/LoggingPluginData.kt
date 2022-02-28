package it.sephiroth.android.library.asm.plugin.logging.vo

import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import org.gradle.api.tasks.Input

class LoggingPluginData(override val replaceTimber: Boolean) : ILoggingPluginData

interface ILoggingPluginData : IPluginData {

    /**
     * Replace all Timber logging methods with Trunk logging methods
     */
    @get:Input
    val replaceTimber: Boolean
}
