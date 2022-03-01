//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.AsmTransformer
import it.sephiroth.android.library.asm.plugin.logging.vo.LoggingPluginData
import org.gradle.api.Project

@Suppress("DEPRECATION")
class AsmLoggingTransformer(project: Project, extensionName: String) :
    AsmTransformer<LoggingPluginExtension, LoggingPluginData, LoggingClassVisitor>(
        project,
        extensionName,
        LoggingPluginExtension::class.java,
        LoggingClassVisitor::class.java
    ) {

    override fun generatePluginData(pluginExtension: LoggingPluginExtension): LoggingPluginData {
        return LoggingPluginData(pluginExtension.replaceTimber)
    }

    override fun processJars(pluginExtension: LoggingPluginExtension): Boolean {
        return false
    }

}
