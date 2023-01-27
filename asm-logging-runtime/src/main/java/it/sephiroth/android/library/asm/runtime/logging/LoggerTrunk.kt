package it.sephiroth.android.library.asm.runtime.logging

import android.util.Log
import androidx.annotation.Keep


/**
 * Must be used together with the asm-logging plugin.
 * If the asm-logging-plugin is not applied, or not enabled, logging
 * from this class will be swallowed and forgotten.
 *
 * @author Alessandro Crugnola on 24.02.22 - 17:19
 */
@Suppress("unused", "UNUSED_PARAMETER")
@Keep
class LoggerTrunk(override val tag: String) : ILogger {

    @JvmSynthetic
    override fun log(message: String, args: Array<out Any?>?, priority: Int, lineNumber: Int) {
        Trunk.log(message, args, priority, tag, lineNumber)
    }

    @JvmSynthetic
    override fun log(throwable: Throwable?, message: String, args: Array<out Any?>?, priority: Int, lineNumber: Int) {
        Trunk.log(throwable, message, args, priority, tag, lineNumber)
    }

    @JvmSynthetic
    override fun log(throwable: Throwable, priority: Int, lineNumber: Int) {
        Trunk.log(throwable, priority, tag, lineNumber)
    }

    // ----------------------------------------------------
    // region API visible methods

    override fun v(message: String, vararg args: Any?) {}

    override fun v(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun v(throwable: Throwable?) {}

    override fun d(message: String, vararg args: Any?) {}

    override fun d(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun d(throwable: Throwable?) {}

    override fun i(message: String, vararg args: Any?) {}

    override fun i(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun i(throwable: Throwable?) {}

    override fun w(message: String, vararg args: Any?) {}

    override fun w(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun w(throwable: Throwable?) {}

    override fun e(message: String, vararg args: Any?) {}

    override fun e(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun e(throwable: Throwable?) {}

    override fun wtf(message: String, vararg args: Any?) {}

    override fun wtf(throwable: Throwable?, message: String?, vararg args: Any?) {}

    override fun wtf(throwable: Throwable?) {}
}
