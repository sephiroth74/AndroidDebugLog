//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.logginglevel

import com.android.build.api.transform.TransformInvocation
import it.sephiroth.android.library.asm.core.AndroidLogLevel
import it.sephiroth.android.library.asm.core.AsmTransformer
import it.sephiroth.android.library.asm.logginglevel.vo.LoggingPluginData
import org.gradle.api.Project

@Suppress("DEPRECATION")
class AsmLoggingLevelTransformer(project: Project, extensionName: String) :
    AsmTransformer<LoggingLevelPluginExtension, LoggingPluginData, LoggingLevelClassVisitor>(project, extensionName, LoggingLevelClassVisitor::class.java) {

    override fun isPluginEnabled(transformInvocation: TransformInvocation, extension: LoggingLevelPluginExtension): Boolean {
        if (extension.minLogLevel.orNull == AndroidLogLevel.VERBOSE) return false
        return super.isPluginEnabled(transformInvocation, extension)
    }

    override fun isPluginEnabledForVariant(extension: LoggingLevelPluginExtension, variantName: String): Boolean {
        val runVariant = extension.runVariant.get()
        return variantName.matches(Regex(runVariant))
    }

    override fun generatePluginData(pluginExtension: LoggingLevelPluginExtension): LoggingPluginData {
        return LoggingPluginData(pluginExtension.minLogLevel.get())
    }

    override fun processJars(pluginExtension: LoggingLevelPluginExtension): Boolean {
        return pluginExtension.includeLibs.getOrElse(false)
    }

}
