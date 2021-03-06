package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.plugin.core.AsmCorePlugin
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginData
import org.gradle.api.Project

class DebugLogPlugin : AsmCorePlugin<DebugLogPluginExtension, DebugLogPluginData, PreClassVisitor>(BuildConfig.EXTENSION_NAME, DebugLogPluginExtension::class.java) {

    override fun getTransformer(project: Project): DebugLogTransformer {
        return DebugLogTransformer(project, extensionName)
    }

}
