package it.sephiroth.android.app.debuglog_example

//import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments
//import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
//import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger
//import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger.DefaultDebugLogHandler
//import it.sephiroth.android.library.asm.runtime.logginglevel.LoggingLevel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogClass
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogSkip
import it.sephiroth.android.library.asm.runtime.logging.Trunk
import timber.log.Timber
import java.io.IOException

@DebugLogClass
class MainActivity : AppCompatActivity() {

    @DebugLog(enabled = BuildConfig.IS_DEBUG)
    fun testMethod(input: Int = 16): Int {
        for (a in 0 until 100) {
            when (@Nullable a) {
                else -> Trunk.d("a = $a")
            }
        }
        @Suppress("unused")
        val i = 1000
        return i
    }

    //    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.FULL, debugExit = true, debugEnter = true, tag = "ciccio")
    @DebugLogSkip
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_main)
    }

    //    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.FULL, debugExit = true, debugEnter = true, tag = "ciccio")
    override fun onResume() {
        super.onResume()
        runTests()
    }

    @SuppressLint("CheckResult")
    fun runTests() {
        val t = Trunk.tag("MY_CUSTOM_TAG")
        t.v("My Custom Message `%s` with `%d` args", "with args", 2)
        t.v(RuntimeException("test"), "My Custom Message `%s` with `%d` args", "with args", 2)
        t.v(RuntimeException("another exception"))

        Completable.create { emitter ->
            Trunk.i("runTests")
            emitter.onComplete()
        }.subscribeOn(Schedulers.computation())
            .subscribe {
                Trunk.d("test completed")
            }

        listOf<String>("hello", "strange", "world").forEach {
            Trunk.once(1_000, Log.ERROR, "(once) this message should appear only once for message: $it")
            Trunk.d("(once) this message should appear only once for message: $it")
            Trunk.e(RuntimeException("(once) test exception"))
            Trunk.w(IOException("test io exception"), "(once) message with args: %s", it)
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val ENABLED = false
    }
}
