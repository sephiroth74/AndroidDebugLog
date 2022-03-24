//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.plugin.core.AsmTransformer
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginData
import org.gradle.api.Project

@Suppress("DEPRECATION")
class DebugLogTransformer(project: Project, extensionName: String) :
    AsmTransformer<DebugLogPluginExtension, DebugLogPluginData, PreClassVisitor>(project, extensionName, DebugLogPluginExtension::class.java, PreClassVisitor::class.java) {

    override fun generatePluginData(pluginExtension: DebugLogPluginExtension): DebugLogPluginData {
        return DebugLogPluginData(pluginExtension.logLevel, pluginExtension.debugExit, pluginExtension.debugArguments)
    }

    override fun processJars(pluginExtension: DebugLogPluginExtension): Boolean {
        return false
    }

}
