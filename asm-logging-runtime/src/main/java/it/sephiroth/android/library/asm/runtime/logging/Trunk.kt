package it.sephiroth.android.library.asm.runtime.logging

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

    // ----------------------------------------------------
    // region API visible methods

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

    // endregion API visible methods

    // ----------------------------------------------------
    // region Replacement methods

    @JvmSynthetic
    @JvmStatic
    fun log(message: String, args: Array<out Any?>?, priority: Int, tag: String) {
        logInternal(priority, null, message, args, tag)
    }

    @JvmSynthetic
    @JvmStatic
    fun log(throwable: Throwable?, message: String, args: Array<out Any?>?, priority: Int, tag: String) {
        logInternal(priority, throwable, message, args, tag)
    }

    @JvmSynthetic
    @JvmStatic
    fun log(throwable: Throwable, priority: Int, tag: String) {
        logInternal(priority, throwable, null, null, tag)
    }


    // endregion Replacement methods

    @JvmStatic
    private fun logInternal(priority: Int, throwable: Throwable?, message: String?, args: Array<out Any?>?, tag: String) {
        if (args != null) {
            prepareLog(tag, priority, throwable, message, *args)
        } else {
            prepareLog(tag, priority, throwable, message)
        }
    }

    private fun prepareLog(tag: String, priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
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

        logToAndroidLog(priority, tag, msg)
    }

    private fun logToAndroidLog(priority: Int, tag: String?, message: String) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, message)
        } else {
            Log.println(priority, tag, message)
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
        return true;
        //return android.util.Log.isLoggable(tag, priority)
    }
}
