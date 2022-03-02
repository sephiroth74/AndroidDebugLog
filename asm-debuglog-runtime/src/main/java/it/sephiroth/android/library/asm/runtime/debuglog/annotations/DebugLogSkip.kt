package it.sephiroth.android.library.asm.runtime.debuglog.annotations

import android.util.Log

/**
 * Skip logging injections
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
annotation class DebugLogSkip
