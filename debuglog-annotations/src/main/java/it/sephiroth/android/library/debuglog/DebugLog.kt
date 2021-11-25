package it.sephiroth.android.library.debuglog

import android.util.Log

/**
 * @param debugResult       if set to true also the return value of the method will be printed
 * @param logLevel          set the log level for the final log output
 * @param debugArguments    defines how the input arguments will be logged (see [DebugArguments])
 * @param enabled           if set to false no output will be produced
 * @param tag               if set to non empty string overrides the default log tag, which will be otherwise the class name
 */
@Suppress("unused")
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DebugLog(
    val debugResult: Boolean = false,
    val logLevel: Int = Log.INFO,
    val debugArguments: Int = DebugArguments.FULL,
    val enabled: Boolean = true,
    val tag: String = "",
)
