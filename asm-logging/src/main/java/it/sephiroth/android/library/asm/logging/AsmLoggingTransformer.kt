//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.logging

import it.sephiroth.android.library.asm.core.AsmTransformer
import it.sephiroth.android.library.asm.logging.vo.LoggingPluginData
import org.gradle.api.Project

@Suppress("DEPRECATION")
class AsmLoggingTransformer(project: Project, extensionName: String) :
    AsmTransformer<LoggingPluginExtension, LoggingPluginData, LoggingClassVisitor>(
        project,
        extensionName,
        LoggingClassVisitor::class.java
    ) {

    override fun generatePluginData(pluginExtension: LoggingPluginExtension): LoggingPluginData {
        return LoggingPluginData()
    }

    override fun processJars(pluginExtension: LoggingPluginExtension): Boolean {
        return false
    }

}
