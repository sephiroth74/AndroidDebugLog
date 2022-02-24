package it.sephiroth.android.library.asm.core

import org.gradle.api.plugins.ExtensionContainer

@Suppress("LeakingThis")
abstract class AsmCoreBasePluginExtension {
    abstract val extensions: ExtensionContainer
}

