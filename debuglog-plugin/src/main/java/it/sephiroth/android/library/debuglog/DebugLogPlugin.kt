package it.sephiroth.android.library.debuglog

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginCollection

class DebugLogPlugin : Plugin<Project> {

    companion object {
        const val TAG = Constants.DEBUGLOG_EXTENSION
    }

    override fun apply(project: Project) {
        val log = project.logger
        val hasApp: PluginCollection<AppPlugin>? = project.plugins.withType(AppPlugin::class.java)
        val hasLib: PluginCollection<LibraryPlugin>? = project.plugins.withType(LibraryPlugin::class.java)

        log.debug("project: ${project.name} hasApp: $hasApp, hasLib: $hasLib")

        if (hasApp == null && hasLib == null) {
            throw IllegalStateException("'android-application' or 'android-library' plugin required.")
        }

        // 1. create the 'debuglog' extension
        project.extensions.create(Constants.DEBUGLOG_EXTENSION, DebugLogPluginExtension::class.java)

        // register the plugin
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.registerTransform(DebugLogTransformer(project))

        log.lifecycle("Registered `${Constants.DEBUGLOG_EXTENSION}` extension for ${this::class.java.simpleName}")
    }

}
