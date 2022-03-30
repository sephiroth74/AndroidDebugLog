package it.sephiroth.android.app.debuglog_example;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * tvlibrary
 *
 * @author Alessandro Crugnola on 24.03.22 - 12:27
 */
public class DebugCursorWrapper extends CursorWrapper {
    private static final String TAG = "DebugCursor";
    private final Throwable mTrace;
    private boolean mIsClosed = false;

    public DebugCursorWrapper(@Nullable Cursor cursor) {
        super(cursor);
        mTrace = new Throwable("Explicit termination method 'close()' not called");
    }

    @Override
    public void close() {
        mIsClosed = true;
    }

    @SuppressLint({"LogNotTrunk", "LogNotTimber"})
    @Override
    public void finalize() throws Throwable {
        try {
            if (!mIsClosed) {
                Log.e(TAG, "Cursor leaks", mTrace);
                printCallStack("DebugCursor");
            }
        } finally {
            super.finalize();
        }
    }

    public static DebugCursorWrapper create(@Nullable Cursor cursor) {
        return new DebugCursorWrapper(cursor);
    }

    @SuppressLint({"LogNotTrunk", "LogNotTimber"})
    private static void printCallStack(String tag) {
        try {
            throw new IllegalArgumentException("printCallbackStack");
        } catch (IllegalArgumentException t) {
            StackTraceElement[] stacktrace = t.getStackTrace();
            for (StackTraceElement element : stacktrace) {
                Log.wtf(tag, String.format("%s:%s (%s::%s)", element.getFileName(), element.getLineNumber(), element.getClassName(), element.getMethodName()));
            }
        }
    }
}
