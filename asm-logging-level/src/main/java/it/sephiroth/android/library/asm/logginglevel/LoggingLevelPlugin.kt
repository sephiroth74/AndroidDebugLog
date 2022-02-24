package it.sephiroth.android.library.asm.logginglevel

import it.sephiroth.android.library.asm.core.AsmCorePlugin
import it.sephiroth.android.library.asm.logginglevel.vo.LoggingPluginData
import org.gradle.api.Project


class LoggingLevelPlugin : AsmCorePlugin<LoggingLevelPluginExtension, LoggingPluginData, LoggingLevelClassVisitor>(
    BuildConfig.EXTENSION_NAME,
    LoggingLevelPluginExtension::class.java
) {

    override fun getTransformer(project: Project): AsmLoggingLevelTransformer {
        return AsmLoggingLevelTransformer(project, extensionName)
    }
}
