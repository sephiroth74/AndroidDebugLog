//file:noinspection GrDeprecatedAPIUsage

@file:Suppress("DEPRECATION")

package it.sephiroth.android.library.asm.plugin.logginglevel

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformInvocation
import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.core.AsmTransformer
import it.sephiroth.android.library.asm.plugin.logginglevel.vo.LoggingPluginData
import org.gradle.api.Project

class AsmLoggingLevelTransformer(project: Project, extensionName: String) :
    AsmTransformer<LoggingLevelPluginExtension, LoggingPluginData, LoggingLevelClassVisitor>(
        project,
        extensionName,
        LoggingLevelPluginExtension::class.java,
        LoggingLevelClassVisitor::class.java
    ) {

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        val scopes = super.getScopes()
        if (pluginExtension.includeLibs) {
            scopes.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        }
        return scopes
    }

    override fun isPluginEnabled(transformInvocation: TransformInvocation, extension: LoggingLevelPluginExtension): Boolean {
        if (extension.minLogLevel <= AndroidLogLevel.VERBOSE) return false
        return super.isPluginEnabled(transformInvocation, extension)
    }

    override fun generatePluginData(pluginExtension: LoggingLevelPluginExtension): LoggingPluginData {
        return LoggingPluginData(pluginExtension.minLogLevel)
    }

    override fun processJars(pluginExtension: LoggingLevelPluginExtension): Boolean {
        return pluginExtension.includeLibs
    }

}
