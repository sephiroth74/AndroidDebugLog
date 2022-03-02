package it.sephiroth.android.library.asm.plugin.debuglog

import it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel

object Constants {

    @Suppress("UNUSED_PARAMETER")
    fun makeTag(obj: Any): String = BuildConfig.EXTENSION_NAME

    //
    // default values for the plugin
    //

    val DEFAULT_LOG_LEVEL = AndroidLogLevel.INFO

    const val DEFAULT_DEBUG_RESULT = false

    val DEFAULT_DEBUG_ARGUMENTS = DebugArguments.Full

    object JavaTypes {
        // from asm-common project

        const val TYPE_PARAMS_LOGGER = "it/sephiroth/android/library/asm/runtime/debuglog/ParamsLogger"
        const val TYPE_RESULT_LOGGER = "it/sephiroth/android/library/asm/runtime/debuglog/MethodResultLogger"

        // from asm-annotations project
        const val TYPE_ANNOTATION_DEBUGLOG = "it/sephiroth/android/library/asm/runtime/debuglog/annotations/DebugLog"
        const val TYPE_ANNOTATION_DEBUGLOG_SKIP = "it/sephiroth/android/library/asm/runtime/debuglog/annotations/DebugLogSkip"
        const val TYPE_ANNOTATION_DEBUGLOG_CLASS = "it/sephiroth/android/library/asm/runtime/debuglog/annotations/DebugLogClass"

        const val TYPE_OBJECT = "java/lang/Object"
    }


}
