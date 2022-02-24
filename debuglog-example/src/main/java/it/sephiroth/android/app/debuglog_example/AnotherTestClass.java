package it.sephiroth.android.app.debuglog_example;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import it.sephiroth.android.library.asm.annotations.debuglog.DebugLogClass;

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

    public int testLog(String message) {
        return Log.i("TAG", message);
    }

    public int testLog(String message, Object... args) {
        if (null != args && args.length > 0)
            return Log.i("TAG", String.format(message, args));
        else
            return Log.i("TAG", message);
    }
}
