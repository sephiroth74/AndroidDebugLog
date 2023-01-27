package it.sephiroth.android.library.asm.runtime.logging

import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import java.io.PrintWriter
import java.io.StringWriter


/**
 * Must be used together with the asm-logging plugin.
 * If the asm-logging-plugin is not applied, or not enabled, logging
 * from this class will be swallowed and forgotten.
 *
 * @author Alessandro Crugnola on 24.02.22 - 17:19
 */
@Suppress("unused", "UNUSED_PARAMETER")
@Keep
object Trunk {
    private const val MAX_LOG_LENGTH = 4000
    private const val MAX_TAG_LENGTH = 23

    /**
     * Can be replaced at runtime to enable/disable logging
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isLoggable: ((String?, Int) -> Boolean)? = null

    // ----------------------------------------------------
    // region API visible methods

    fun tag(tag: String): ILogger = LoggerTrunk(tag)

    @JvmStatic
    fun v(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun v(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun v(throwable: Throwable?) {
    }

    @JvmStatic
    fun d(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun d(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun d(throwable: Throwable?) {
    }

    @JvmStatic
    fun i(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun i(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun i(throwable: Throwable?) {
    }

    @JvmStatic
    fun w(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun w(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun w(throwable: Throwable?) {
    }

    @JvmStatic
    fun e(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun e(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun e(throwable: Throwable?) {
    }

    @JvmStatic
    fun wtf(message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun wtf(throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun wtf(throwable: Throwable?) {
    }

    @JvmStatic
    fun once(
        code: Int,
        @androidx.annotation.IntRange(
            from = Log.VERBOSE.toLong(),
            to = Log.ASSERT.toLong()
        ) priority: Int,
        message: String?,
        vararg args: Any?
    ) {
    }

    @JvmStatic
    fun once(code: Int, priority: Int, throwable: Throwable?, message: String?, vararg args: Any?) {
    }

    @JvmStatic
    fun once(code: Int, priority: Int, throwable: Throwable?) {
    }

    // endregion API visible methods

    // ----------------------------------------------------
    // region Replacement methods

    private val codeCache = hashSetOf<Int>()

    @JvmSynthetic
    @JvmStatic
    fun logOnce(
        code: Int,
        priority: Int,
        message: String?,
        vararg args: Any?,
        tag: String,
        lineNumber: Int
    ) {
        if (codeCache.add(code)) {
            logInternal(priority, null, message, args, tag, lineNumber)
        }
    }

    @JvmSynthetic
    @JvmStatic
    fun logOnce(
        code: Int,
        priority: Int,
        throwable: Throwable?,
        message: String,
        args: Array<out Any?>?,
        tag: String,
        lineNumber: Int,
    ) {
        if (codeCache.add(code)) {
            logInternal(priority, throwable, message, args, tag, lineNumber)
        }
    }

    @JvmSynthetic
    @JvmStatic
    fun logOnce(
        code: Int,
        priority: Int,
        throwable: Throwable,
        tag: String,
        lineNumber: Int
    ) {
        if (codeCache.add(code)) {
            logInternal(priority, throwable, null, null, tag, lineNumber)
        }
    }


    @JvmSynthetic
    @JvmStatic
    fun log(
        message: String,
        args: Array<out Any?>?,
        priority: Int,
        tag: String,
        lineNumber: Int
    ) {
        logInternal(priority, null, message, args, tag, lineNumber)
    }

    @JvmSynthetic
    @JvmStatic
    fun log(
        throwable: Throwable?,
        message: String,
        args: Array<out Any?>?,
        priority: Int,
        tag: String,
        lineNumber: Int
    ) {
        logInternal(priority, throwable, message, args, tag, lineNumber)
    }

    @JvmSynthetic
    @JvmStatic
    fun log(
        throwable: Throwable,
        priority: Int,
        tag: String,
        lineNumber: Int
    ) {
        logInternal(priority, throwable, null, null, tag, lineNumber)
    }


    // endregion Replacement methods

    @JvmStatic
    private fun logInternal(
        priority: Int,
        throwable: Throwable?,
        message: String?,
        args: Array<out Any?>?,
        tag: String,
        lineNumber: Int
    ) {
        if (args != null) {
            prepareLog(tag, lineNumber, priority, throwable, message, *args)
        } else {
            prepareLog(tag, lineNumber, priority, throwable, message)
        }
    }

    private fun prepareLog(
        tag: String,
        lineNumber: Int,
        priority: Int,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        if (!isLoggable(tag, priority)) {
            return
        }

        var msg = message
        if (msg.isNullOrEmpty()) {
            if (t == null) {
                return  // Swallow message if it's null and there's no throwable.
            }
            msg = getStackTraceString(t)
        } else {
            if (args.isNotEmpty()) {
                msg = formatMessage(msg, args)
            }
            if (t != null) {
                msg += "\n" + getStackTraceString(t)
            }
        }

        logToAndroidLog(priority, reduceTag(tag), "[$lineNumber] $msg")
    }

    private fun reduceTag(tag: String): String {
        return if (Build.VERSION.SDK_INT >= 26 || tag.length <= MAX_TAG_LENGTH) tag
        else tag.substring(0, MAX_TAG_LENGTH)
    }

    private fun logToAndroidLog(priority: Int, tag: String?, message: String) {
        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
            return
        }

        // Split by line to fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }

    private fun formatMessage(message: String, args: Array<out Any?>) = message.format(*args)

    private fun getStackTraceString(t: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun isLoggable(tag: String?, priority: Int): Boolean {
        return isLoggable?.invoke(tag, priority) ?: true
    }
}
