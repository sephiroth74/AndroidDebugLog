package it.sephiroth.android.app.debuglog_example;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

import it.sephiroth.android.library.asm.runtime.logging.Trunk;
import timber.log.Timber;

/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 28.02.22 - 16:01
 */
@SuppressLint("LogNotTimber")
class TestLoggingLevel {
    public static void test() {
        testAndroidLog();
        testTimberLog();
        testTrunkLog();

        testReturnCursor();
        testReturnMatrixCursor();
    }

    private static void testAndroidLog() {
        Log.wtf(TAG, "test wtf");
        Log.e(TAG, "test e");
        Log.w(TAG, "test w");
        Log.i(TAG, "test i");
        Log.d(TAG, "test d");
        Log.v(TAG, "test v");

        Log.v(TAG, "test exception v", new RuntimeException());
        Log.w(TAG, new RuntimeException("test exception w"));
        Log.e(TAG, "test exception e", new RuntimeException());
    }

    private static void testTimberLog() {
        Timber.wtf("test timber wtf");
        Timber.e("test timber e");
        Timber.w("test timber w");
        Timber.i("test timber i");
        Timber.d("test timber d");
        Timber.v("test timber v");

        Timber.v(new RuntimeException(), "test timber v exception");
        Timber.w(new RuntimeException("test timber w exception"));
        Timber.e(new RuntimeException(), "test timber e exception");
    }

    private static void testTrunkLog() {
        Trunk.wtf("test trunk wtf");
        Trunk.e("test trunk e");
        Trunk.w("test trunk w");
        Trunk.i("test trunk i");
        Trunk.d("test trunk d");
        Trunk.v("test trunk v");

        Trunk.v(new RuntimeException(), "test trunk exception");
        Trunk.w(new RuntimeException());
        Trunk.e(new RuntimeException(), "test exception");
    }

    public static Cursor testReturnCursor() {
        Cursor c = new MatrixCursor(new String[]{"ID"});
        ((MatrixCursor) c).newRow().add(1);
        return c;
    }

    public static MatrixCursor testReturnMatrixCursor() {
        MatrixCursor c = new MatrixCursor(new String[]{"ID"});
        c.newRow().add(1);
        return DebugCursorWrapper.create(c);
    }

    static final String TAG = TestLoggingLevel.class.getSimpleName();
}
