package it.sephiroth.android.library.asm.commons.plugin

import org.gradle.api.plugins.ExtensionContainer

@Suppress("LeakingThis")
abstract class AsmBaseExtension {
    abstract val extensions: ExtensionContainer
}
