package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AndroidLogLevel

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
        const val TYPE_PARAMS_LOGGER = "it/sephiroth/android/library/asm/commons/logging/ParamsLogger"
        const val TYPE_RESULT_LOGGER = "it/sephiroth/android/library/asm/commons/logging/MethodResultLogger"

        // from asm-annotations project
        const val TYPE_ANNOTATION_DEBUGLOG = "it/sephiroth/android/library/asm/annotations/debuglog/DebugLog"
        const val TYPE_ANNOTATION_DEBUGLOG_CLASS = "it/sephiroth/android/library/asm/annotations/debuglog/DebugLogClass"

        const val TYPE_OBJECT = "java/lang/Object"
    }


}
