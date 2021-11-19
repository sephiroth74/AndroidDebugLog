package it.sephiroth.android.library.debuglog

import org.objectweb.asm.Opcodes

object Constants {
    const val ASM_VERSION = BuildConfig.ASM_VERSION

    const val DEBUGLOG_EXTENSION = "androidDebugLog"


    //
    // default values for the plugin
    //

    val DEFAULT_LOG_LEVEL = AndroidLogLevel.INFO

    const val DEFAULT_DEBUG_RESULT = false

    val DEFAULT_DEBUG_ARGUMENTS = DebugArguments.Full

    object JavaTypes {
        const val TYPE_PARAMS_LOGGER = "it/sephiroth/android/library/debuglog/ParamsLogger"
        const val TYPE_RESULT_LOGGER = "it/sephiroth/android/library/debuglog/MethodResultLogger"
        const val TYPE_ANNOTATION_DEBUGLOG = "it/sephiroth/android/library/debuglog/DebugLog"
        const val TYPE_ANNOTATION_DEBUGLOG_CLASS = "it/sephiroth/android/library/debuglog/DebugLogClass"
        const val TYPE_OBJECT = "java/lang/Object"
    }


}
