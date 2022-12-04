package it.sephiroth.android.library.asm.commons

import org.jetbrains.kotlin.tooling.core.withLinearClosure


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 22.02.22 - 18:58
 */

object Constants {
    const val ASM_VERSION = BuildConfig.ASM_VERSION
    const val BASE_EXTENSION_NAME = "androidASM"

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = "$BASE_EXTENSION_NAME [${obj::class.java.simpleName}]"
}
