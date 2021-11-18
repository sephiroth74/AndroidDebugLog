package it.sephiroth.android.library.debuglog

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class DebugLogClass(val debugResult: Boolean, val logLevel: Int, val debugArguments: Int, val enabled: Boolean)
