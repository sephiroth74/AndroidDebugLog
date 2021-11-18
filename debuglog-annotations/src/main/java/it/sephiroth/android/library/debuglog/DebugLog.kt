package it.sephiroth.android.library.debuglog

import android.util.Log

@Suppress("unused")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CONSTRUCTOR)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class DebugLog(val debugResult: Boolean = false, val logLevel: Int = Log.INFO, val debugArguments: Int = DebugArguments.FULL, val enabled: Boolean = true)
