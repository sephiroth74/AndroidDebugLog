package it.sephiroth.android.library.debuglog;

import android.util.Log;

/**
 * Created by quinn on 2019/1/8
 */
@SuppressWarnings("unused")
public class DebugLogger {
    public static final DebugLogHandler NullLogger = new DebugLogHandler();
    public static final DebugLogHandler DefaultLogger;

    static {
        DefaultLogger = new DebugLogHandler() {

            @Override
            protected void logEnter(int priority, String tag, String methodName, String params) {
                Log.println(priority, tag, String.format("\u21E2 %s[%s]", methodName, params)); // ⇢
            }

            @Override
            protected void logExit(int priority, String tag, String methodName, long costedMillis, String result) {
                Log.println(priority, tag, String.format("\u21E0 %s[%sms] = %s", methodName, costedMillis, result)); // ⇠
            }
        };
    }

    static DebugLogHandler DEFAULT_IMPL = DefaultLogger;

    public static void installLog(DebugLogHandler loggerHandler) {
        DEFAULT_IMPL = loggerHandler;
    }

}
