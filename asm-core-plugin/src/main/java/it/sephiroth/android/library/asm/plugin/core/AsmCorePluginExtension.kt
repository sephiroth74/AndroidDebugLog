package it.sephiroth.android.library.asm.plugin.core

import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class AsmCorePluginExtension {

    /**
     * If not enabled the plugin will not perform any transformation
     */
    var enabled: Boolean = true

    /**
     * Enable this plugin for the specified variant(s)
     */
    var runVariant: String = ".*"

}
