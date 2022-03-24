package it.sephiroth.android.library.asm.runtime.logginglevel

import android.util.Log


/**
 * Log nothing, as its name suggests...
 *
 * @author Alessandro Crugnola on 23.02.22 - 10:33
 */
@Suppress("UNUSED_PARAMETER", "unused")
object NoLog {
    @JvmStatic
    fun v(tag: String?, msg: String): Int = 0

    @JvmStatic
    fun v(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun d(tag: String?, msg: String): Int = 0

    @JvmStatic
    fun d(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun i(tag: String?, msg: String): Int = 0

    @JvmStatic
    fun i(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun w(tag: String?, msg: String): Int = 0

    @JvmStatic
    fun w(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun w(tag: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun e(tag: String?, msg: String): Int = 0

    @JvmStatic
    fun e(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun wtf(tag: String?, msg: String?): Int = 0

    @JvmStatic
    fun wtf(tag: String?, tr: Throwable): Int = 0

    @JvmStatic
    fun wtf(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic
    fun println(priority: Int, tag: String?, msg: String): Int {
        return if (LoggingLevel.isLoggable(tag, priority)) Log.println(priority, tag, msg) else 0
    }
}
