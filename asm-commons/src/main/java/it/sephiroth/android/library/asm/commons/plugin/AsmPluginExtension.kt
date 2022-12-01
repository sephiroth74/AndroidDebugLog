package it.sephiroth.android.library.asm.commons.plugin

@Suppress("LeakingThis")
abstract class AsmPluginExtension {

    /**
     * If not enabled the plugin will not perform any transformation
     */
    var enabled: Boolean = true

    /**
     * Enable this plugin for the specified variant(s)
     */
    var runVariant: String = ".*"

}
