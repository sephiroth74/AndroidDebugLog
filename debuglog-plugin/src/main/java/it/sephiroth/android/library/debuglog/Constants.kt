package it.sephiroth.android.library.debuglog

import org.objectweb.asm.Opcodes

object Constants {
    const val VERSION_NAME = "0.0.1-SNAPSHOT"

    val ASM_VERSION = Opcodes.ASM7

    const val DEBUGLOG_EXTENSION = "androidDebugLog"


    const val DEBUGLOG_ANNOTATION_DEPENDENCY_LIBRARY_NAME =
        "it.sephiroth.android.library.debuglog:debuglog-annotations"

    //
    // default values for the plugin
    //

    val DEFAULT_LOG_LEVEL = AndroidLogLevel.INFO

    val DEFAULT_DEBUG_RESULT = false

    val DEFAULT_DEBUG_ARGUMENTS = DebugArguments.Full

    object JavaTypes {
        const val TYPE_PARAMS_LOGGER = "it/sephiroth/android/library/debuglog/ParamsLogger"
        const val TYPE_RESULT_LOGGER = "it/sephiroth/android/library/debuglog/MethodResultLogger"
        const val TYPE_ANNOTATION_DEBUGLOG = "it/sephiroth/android/library/debuglog/DebugLog"
        const val TYPE_ANNOTATION_DEBUGLOG_CLASS = "it/sephiroth/android/library/debuglog/DebugLogClass"
        const val TYPE_OBJECT = "java/lang/Object"
    }


}
