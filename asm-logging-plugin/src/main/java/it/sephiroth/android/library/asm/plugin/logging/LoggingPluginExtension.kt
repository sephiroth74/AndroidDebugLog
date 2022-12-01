package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.commons.plugin.AsmPluginExtension

@Suppress("LeakingThis")
abstract class LoggingPluginExtension : AsmPluginExtension() {

    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, runVariant=${runVariant})"
    }
}
