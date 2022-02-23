package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AsmCorePlugin
import it.sephiroth.android.library.asm.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.debuglog.asm.vo.DebugLogPluginData
import org.gradle.api.Project

class DebugLogPlugin : AsmCorePlugin<DebugLogPluginExtension, DebugLogPluginData, PreClassVisitor>(Constants.DEBUGLOG_EXTENSION, DebugLogPluginExtension::class.java) {

    companion object {
        const val TAG = Constants.DEBUGLOG_EXTENSION
    }


    override fun getTransformer(project: Project): DebugLogTransformer {
        return DebugLogTransformer(project, extensionName)
    }

}
