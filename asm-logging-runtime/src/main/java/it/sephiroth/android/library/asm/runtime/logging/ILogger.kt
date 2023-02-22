package it.sephiroth.android.library.asm.runtime.logging

import android.util.Log
import androidx.annotation.IntRange

interface ILogger {
    val tag: String
    fun v(message: String, vararg args: Any?)
    fun v(throwable: Throwable?, message: String?, vararg args: Any?)
    fun v(throwable: Throwable?)
    fun d(message: String, vararg args: Any?)
    fun d(throwable: Throwable?, message: String?, vararg args: Any?)
    fun d(throwable: Throwable?)
    fun i(message: String, vararg args: Any?)
    fun i(throwable: Throwable?, message: String?, vararg args: Any?)
    fun i(throwable: Throwable?)
    fun w(message: String, vararg args: Any?)
    fun w(throwable: Throwable?, message: String?, vararg args: Any?)
    fun w(throwable: Throwable?)
    fun e(message: String, vararg args: Any?)
    fun e(throwable: Throwable?, message: String?, vararg args: Any?)
    fun e(throwable: Throwable?)
    fun wtf(message: String, vararg args: Any?)
    fun wtf(throwable: Throwable?, message: String?, vararg args: Any?)
    fun wtf(throwable: Throwable?)

    // internal api

    @JvmSynthetic
    fun log(
        message: String,
        args: Array<out Any?>?,
        priority: Int,
        lineNumber: Int
    )

    @JvmSynthetic
    fun log(
        throwable: Throwable?,
        message: String,
        args: Array<out Any?>?,
        priority: Int,
        lineNumber: Int
    )


    @JvmSynthetic
    fun log(
        throwable: Throwable,
        priority: Int,
        lineNumber: Int
    )
}
