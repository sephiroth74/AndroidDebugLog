package it.sephiroth.android.app.debuglog_example

import it.sephiroth.android.library.asm.runtime.logginglevel.LoggingLevel
import timber.log.Timber


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 02.03.22 - 14:52
 */
class TimberTree : Timber.DebugTree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return LoggingLevel.isLoggable(tag, priority)
    }
}
