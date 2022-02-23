package it.sephiroth.android.library.asm.debuglog

import it.sephiroth.android.library.asm.core.AndroidLogLevel

object Constants {
    const val DEBUGLOG_EXTENSION = "androidAsmDebugLog"

    fun makeTag(obj: Any): String {
        return DEBUGLOG_EXTENSION
    }

    //
    // default values for the plugin
    //

    val DEFAULT_LOG_LEVEL = AndroidLogLevel.INFO

    const val DEFAULT_DEBUG_RESULT = false

    val DEFAULT_DEBUG_ARGUMENTS = DebugArguments.Full

    object JavaTypes {
        const val TYPE_PARAMS_LOGGER = "it/sephiroth/android/library/asm/annotations/debuglog/ParamsLogger"
        const val TYPE_RESULT_LOGGER = "it/sephiroth/android/library/asm/annotations/debuglog/MethodResultLogger"
        const val TYPE_ANNOTATION_DEBUGLOG = "it/sephiroth/android/library/asm/annotations/debuglog/DebugLog"
        const val TYPE_ANNOTATION_DEBUGLOG_CLASS = "it/sephiroth/android/library/asm/annotations/debuglog/DebugLogClass"
        const val TYPE_OBJECT = "java/lang/Object"
    }


}
