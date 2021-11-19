package it.sephiroth.android.app.debuglog_example;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import it.sephiroth.android.library.debuglog.DebugLogClass;

/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 19.11.21 - 18:14
 */
@DebugLogClass(debugResult = false, logLevel = Log.INFO)
class AnotherTestClass {
    public void execute(Runnable action) {
        action.run();
    }

    public void testAll(@NotNull Context context) {
        execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
