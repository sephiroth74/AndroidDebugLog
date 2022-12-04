package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.plugin.AsmBaseExtension
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodAnnotationData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

abstract class DebugLogPluginInit : Plugin<Project> {
    private lateinit var logger: Logger
    private val extensionName = BuildConfig.EXTENSION_NAME

    fun getObjectMapping() = map

    override fun apply(target: Project) {
        logger = target.logger
        logger.lifecycle("[DebugLogPluginInit] apply(${target.name})")

        val base = if (target.extensions.findByName(Constants.BASE_EXTENSION_NAME) == null) {
            logger.lifecycle("[DebugLogPluginInit] registering extension ${Constants.BASE_EXTENSION_NAME}")
            target.extensions.create(Constants.BASE_EXTENSION_NAME, AsmBaseExtension::class.java)
        } else {
            logger.lifecycle("[DebugLogPluginInit] extension ${Constants.BASE_EXTENSION_NAME} already registered")
            target.extensions.getByType(AsmBaseExtension::class.java)
        }

        logger.lifecycle("[DebugLogPluginInit] registering extension ${Constants.BASE_EXTENSION_NAME}.$extensionName")
        base.extensions.create(extensionName, DebugLogPluginExtension::class.java)

//        target.plugins.apply(DebugLogPluginPost::class.java)
        target.plugins.apply(DebugLogPluginPre::class.java)
    }

    companion object {
        val map = hashMapOf<String, HashMap<String, Pair<MethodAnnotationData, List<MethodParameter>>>>()
    }
}
