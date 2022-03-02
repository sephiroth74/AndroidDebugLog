package it.sephiroth.android.app.debuglog_example

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.sephiroth.android.library.asm.runtime.logging.Trunk
import timber.log.Timber

@SuppressLint("LogNotTimber")
class MainActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
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
}
