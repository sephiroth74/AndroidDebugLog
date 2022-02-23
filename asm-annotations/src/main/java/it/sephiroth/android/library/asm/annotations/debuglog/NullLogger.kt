package it.sephiroth.android.library.asm.annotations.debuglog

import android.util.Log


/**
 * @author Alessandro Crugnola on 23.02.22 - 10:33
 */
@Suppress("UNUSED_PARAMETER", "unused")
object NullLogger {
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
    fun println(priority: Int, tag: String?, msg: String): Int = 0

    @JvmStatic
    fun println(priority: Int, tag: String?, msg: String, minPriority: Int): Int {
        return if (priority >= minPriority) Log.println(priority, tag, msg) else 0
    }

    @JvmStatic
    fun isLoggable(priority: Int, minPriority: Int): Boolean = priority >= minPriority

    @JvmStatic
    fun isLoggable(msg: String?, priority: Int, minPriority: Int): Boolean = priority >= minPriority
}
