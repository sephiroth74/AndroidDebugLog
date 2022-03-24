package it.sephiroth.android.library.asm.plugin.logginglevel

import it.sephiroth.android.library.asm.plugin.core.AsmCorePlugin
import it.sephiroth.android.library.asm.plugin.logginglevel.vo.LoggingPluginData
import org.gradle.api.Project


class LoggingLevelPlugin : AsmCorePlugin<LoggingLevelPluginExtension, LoggingPluginData, LoggingLevelClassVisitor>(
    BuildConfig.EXTENSION_NAME,
    LoggingLevelPluginExtension::class.java
) {

    override fun getTransformer(project: Project): AsmLoggingLevelTransformer {
        return AsmLoggingLevelTransformer(project, extensionName)
    }
}
