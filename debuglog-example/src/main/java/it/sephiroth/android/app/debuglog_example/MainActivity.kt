package it.sephiroth.android.app.debuglog_example

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogClass
import timber.log.Timber

@SuppressLint("LogNotTimber")
@DebugLogClass
class MainActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.activity_main)

        Timber.tag("ciccio-pasticcio").v("[timber] test ciccio pasticcio")
    }

    @DebugLog
    override fun onResume() {
        super.onResume()
//        Timber.d("onResume")
//        TestClass().testAll(this)
        AnotherTestClass().testAll(this)
        TestClass().testLog()
        TestLoggingLevel.test()
//        testPrintLn()
//        testLog("message")
    }

    private fun testPrintLn(): Int {
        Log.println(Log.VERBOSE, TAG, "using println(" + System.currentTimeMillis() + ")")
        Log.println(Log.DEBUG, "MainActivity_DEBUG", "using println( " + 2 + ")")
        Log.println(Log.INFO, "MainActivity_INFO", "using println()")
        Log.println(Log.WARN, "MainActivity_WARN", "using println()")
        Log.println(Log.ERROR, "MainActivity_ERROR", "using println()")
        return 0
    }

    private fun testLog(msg: String) {
        Log.i("tag", msg)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
