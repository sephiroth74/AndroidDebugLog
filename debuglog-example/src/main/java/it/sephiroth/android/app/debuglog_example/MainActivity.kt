package it.sephiroth.android.app.debuglog_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.sephiroth.android.library.asm.annotations.debuglog.DebugLogClass
import it.sephiroth.android.library.asm.annotations.debuglog.NullLogger
import timber.log.Timber
import java.io.IOException

@DebugLogClass
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tree = object : Timber.DebugTree() {}

        Timber.plant(tree)

        setContentView(R.layout.activity_main)

        NullLogger.println(Log.VERBOSE, "a", "b", Log.DEBUG)

        Log.v("MainActivity", "onCreate(VERBOSE)")
        Log.v("MainActivity", "onCreate(VERBOSE)")
        Log.d("MainActivity", "onCreate(DEBUG)")
        Log.i("MainActivity", "onCreate(INFO)")
        Log.w("MainActivity", "onCreate(WARNING)")
        Log.e("MainActivity", "onCreate(ERROR)")
        Log.wtf("MainActivity", "onCreate(WTF)")

        Timber.wtf("Timber($this)" + System.currentTimeMillis())
        Timber.e("Timber($this)")
        Timber.w("Timber($this)")
        Timber.i("Timber($this)")
        Timber.d("Timber($this)")
        Timber.v("Timber($this)" + System.currentTimeMillis())

        Timber.tag("ciccio-pasticcio").v("test ciccio pasticcio")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        TestClass().testAll(this)
        AnotherTestClass().testAll(this)
    }

    fun testPrintLn(): Int {
//        Log.println(getLogLevel(), TAG, "using println(" + System.currentTimeMillis() + ")")
        Log.println(Log.VERBOSE, TAG, "using println(" + System.currentTimeMillis() + ")")
        Log.println(Log.DEBUG, "MainActivity_DEBUG", "using println( " + 2 + ")")
        Log.println(Log.INFO, "MainActivity_INFO", "using println()")
        Log.println(Log.WARN, "MainActivity_WARN", "using println()")
        Log.println(Log.ERROR, "MainActivity_ERROR", "using println()")
//        Log.println(Log.ASSERT, "MainActivity_ASSERT", "using println()")
        return 0
    }

    fun getLogLevel(): Int = Log.VERBOSE

    companion object {
        const val TAG = "MainActivity"
    }
}
