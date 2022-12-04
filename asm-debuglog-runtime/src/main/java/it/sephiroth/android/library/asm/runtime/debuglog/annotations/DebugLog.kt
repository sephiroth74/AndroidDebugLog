package it.sephiroth.android.library.asm.runtime.debuglog.annotations

import android.util.Log

/**
 * @param debugExit       if set to true it will log the method exit
 * @param debugEnter        if set to true it will log the method enter
 * @param logLevel          set the log level for the final log output
 * @param debugArguments    defines how the input arguments will be logged (see [DebugArguments])
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
    val debugExit: Boolean = false,
    val debugEnter: Boolean = true,
    val logLevel: Int = Log.INFO,
    val debugArguments: Int = DebugArguments.FULL,
    val tag: String = "",
)
