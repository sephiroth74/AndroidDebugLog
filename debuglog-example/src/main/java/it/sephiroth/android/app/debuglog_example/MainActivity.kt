package it.sephiroth.android.app.debuglog_example

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger
import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger.DefaultDebugLogHandler
import it.sephiroth.android.library.asm.runtime.logging.Trunk
import it.sephiroth.android.library.asm.runtime.logginglevel.LoggingLevel
import timber.log.Timber

@SuppressLint("LogNotTimber")
class MainActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, """MIN_LOG_LEVEL = ${LoggingLevel.getMinPriority()}""")

        Trunk.isLoggable = { tag, priority -> LoggingLevel.isLoggable(tag, priority) }

        DebugLogger.installLog(object : DefaultDebugLogHandler() {

            override fun isLoggable(tag: String?, priority: Int): Boolean {
                return LoggingLevel.isLoggable(tag, priority)
            }
        })

        super.onCreate(savedInstanceState)
        Timber.plant(TimberTree())
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        runTests()
    }

    private fun runTests() {
        Completable.create { emitter ->
            Trunk.i("runTests")
            AnotherTestClass().test(this)
            TestClass().test(this)
            TestLoggingLevel.test()
            TestDebugLog().test(this)

            emitter.onComplete()
        }.subscribeOn(Schedulers.computation()).subscribe {
            Log.d(TAG, "test completed")
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
//
//    object TTTT {
//        private val MIN_PRIORITY: Int = Log.VERBOSE
//
//        @Suppress("unused")
//        fun getMinPriority(): Int {
//            return Log.ASSERT
//        }
//
//        @Suppress("UNUSED_PARAMETER")
//        @JvmStatic
//        fun isLoggable(tag: String?, priority: Int) = priority >= getMinPriority()
//    }
}
