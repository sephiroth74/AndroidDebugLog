package it.sephiroth.android.library.asm.plugin.logging

import it.sephiroth.android.library.asm.plugin.core.AsmCorePluginExtension

@Suppress("LeakingThis")
abstract class LoggingPluginExtension : AsmCorePluginExtension() {
    var replaceTimber: Boolean = Constants.DEFAULT_REPLACE_TIMBER

    override fun toString(): String {
        return "${BuildConfig.EXTENSION_NAME}(enabled=${enabled}, replaceTimber=${replaceTimber}, runVariant=${runVariant})"
    }
}
