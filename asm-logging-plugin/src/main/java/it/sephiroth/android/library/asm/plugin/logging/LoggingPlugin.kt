package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.AsmCorePlugin
import it.sephiroth.android.library.asm.plugin.logging.vo.LoggingPluginData
import org.gradle.api.Project


class LoggingPlugin : AsmCorePlugin<LoggingPluginExtension, LoggingPluginData, LoggingClassVisitor>(
    BuildConfig.EXTENSION_NAME,
    LoggingPluginExtension::class.java
) {

    override fun getTransformer(project: Project): AsmLoggingTransformer {
        return AsmLoggingTransformer(project, extensionName)
    }
}
