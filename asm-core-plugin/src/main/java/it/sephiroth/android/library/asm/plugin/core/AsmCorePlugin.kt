package it.sephiroth.android.library.asm.plugin.core

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginCollection
import org.gradle.api.provider.Property

abstract class AsmCorePlugin<T, R, Q>(
    protected val extensionName: String,
    protected val extensionClass: Class<T>
) : Plugin<Project> where T : AsmCorePluginExtension, R : IPluginData, Q : AsmClassVisitor {

    override fun apply(project: Project) {
        val logger = project.logger
        val hasApp: PluginCollection<AppPlugin> = project.plugins.withType(AppPlugin::class.java)
        val hasLib: PluginCollection<LibraryPlugin> = project.plugins.withType(LibraryPlugin::class.java)

        logger.debug("project: ${project.name} hasApp: $hasApp, hasLib: $hasLib")

        if (hasApp.isEmpty() && hasLib.isEmpty()) {
            logger.warn("'com.android.application' or 'com.android.library' plugin required.")
            return
            //throw IllegalStateException("'android-application' or 'android-library' plugin required.")
        }

        // create the extension at project level
        val base = if (project.extensions.findByName(Constants.BASE_EXTENSION_NAME) == null) {
            project.extensions.create(Constants.BASE_EXTENSION_NAME, AsmCoreBasePluginExtension::class.java)
        } else {
            project.extensions.getByType(AsmCoreBasePluginExtension::class.java)
        }

        logger.lifecycle("[${extensionName}] registering ${Constants.BASE_EXTENSION_NAME}.$extensionName")
        base.extensions.create(extensionName, extensionClass)

        // register the plugin
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.registerTransform(getTransformer(project))

        logger.debug("Registered `${extensionName}` extension for ${extensionClass.simpleName}")
    }

    abstract fun getTransformer(project: Project): AsmTransformer<T, R, Q>
}
