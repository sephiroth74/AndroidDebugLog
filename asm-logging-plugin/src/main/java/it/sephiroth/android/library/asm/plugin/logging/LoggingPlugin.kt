package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.AsmCorePlugin
import it.sephiroth.android.library.asm.plugin.logging.vo.LoggingPluginData
import org.gradle.api.Project


class LoggingPlugin : AsmCorePlugin<LoggingPluginExtension, LoggingPluginData, LoggingClassVisitor>(
    BuildConfig.EXTENSION_NAME,
    LoggingPluginExtension::class.java
) {

    override fun apply(project: Project) {
        super.apply(project)
//        project.configurations.forEach { configuration ->
//            println("configuration: ${configuration.name}")
//            configuration.dependencies.forEach { dependency ->
//                println("${dependency.group}:${dependency.name}:${dependency.version}")
//            }
//        }
    }

    override fun getTransformer(project: Project): AsmLoggingTransformer {
        return AsmLoggingTransformer(project, extensionName)
    }
}
