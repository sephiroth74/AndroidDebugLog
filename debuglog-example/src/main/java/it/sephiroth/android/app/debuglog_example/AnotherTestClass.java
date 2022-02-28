package it.sephiroth.android.app.debuglog_example;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogClass;
import it.sephiroth.android.library.asm.runtime.logging.Trunk;
import timber.log.Timber;

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
        execute(() -> {
            testLog1("test message: " + Log.isLoggable("AnotherTestClass", Log.INFO));
            testLog2("second test: %s, %s", "arg1", "arg2");
        });
    }

    public int testLog1(String message) {
        Trunk.v(message);
//        SimpleLog.v(new IOException("test io exception"), message);
//        SimpleLog.v(new RuntimeException("runtime exception"));

        Timber.v("[timber] " + message);
        Timber.v(new IOException("test io exception"), "[timber] " + message + " with exception");
        Timber.v(new RuntimeException("[timber] warning exception"));


        return 0;
    }

    public int testLog2(String message, Object... args) {
        Timber.v("[timber] " + message, args);
//        SimpleLog.v(message, args);
//        SimpleLog.v(new RuntimeException("testing runtime exception"), message, args);
//        Timber.v(message, args);
        return 0;
    }

    private static String TAG = "AnotherTestClass";
}
