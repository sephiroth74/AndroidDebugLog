package it.sephiroth.android.library.asm.core

import org.gradle.api.provider.Property

@Suppress("LeakingThis")
abstract class AsmCorePluginExtension {
    abstract val enabled: Property<Boolean>

    init {
        enabled.convention(true)
    }
}
