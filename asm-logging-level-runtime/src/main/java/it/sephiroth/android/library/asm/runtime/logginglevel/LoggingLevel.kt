package it.sephiroth.android.library.asm.runtime.logginglevel

import android.util.Log


/**
 * @author Alessandro Crugnola on 02.03.22 - 14:57
 */
object LoggingLevel {
    private const val MIN_PRIORITY: Int = Log.VERBOSE

    @Suppress("unused")
    fun getMinPriority(): Int {
        return MIN_PRIORITY
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun isLoggable(tag: String?, priority: Int) = priority >= getMinPriority()
}
