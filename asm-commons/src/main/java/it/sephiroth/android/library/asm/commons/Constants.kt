package it.sephiroth.android.library.asm.commons


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 22.02.22 - 18:58
 */

object Constants {
    const val ASM_VERSION = BuildConfig.ASM_VERSION

    const val BASE_EXTENSION_NAME = "androidASM"

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BASE_EXTENSION_NAME

    object JavaTypes {
        const val TYPE_OBJECT = "java/lang/Object"
    }
}
