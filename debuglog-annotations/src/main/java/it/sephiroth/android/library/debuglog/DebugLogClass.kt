package it.sephiroth.android.library.debuglog

import android.util.Log

@Suppress("unused")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class DebugLogClass(val debugResult: Boolean = false, val logLevel: Int = Log.INFO, val debugArguments: Int = DebugArguments.FULL, val enabled: Boolean = true)
