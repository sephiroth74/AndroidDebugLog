//file:noinspection GrDeprecatedAPIUsage

package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AsmTransformer
import it.sephiroth.android.library.asm.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.debuglog.asm.vo.DebugLogPluginData
import org.gradle.api.Project

@Suppress("DEPRECATION")
class DebugLogTransformer(project: Project, extensionName: String) : AsmTransformer<DebugLogPluginExtension, DebugLogPluginData, PreClassVisitor>(project, extensionName, PreClassVisitor::class.java) {

    override fun generatePluginData(pluginExtension: DebugLogPluginExtension): DebugLogPluginData {
        return DebugLogPluginData(pluginExtension.logLevel, pluginExtension.debugResult, pluginExtension.debugArguments)
    }

    override fun processJars(pluginExtension: DebugLogPluginExtension): Boolean {
        return false
    }

}
