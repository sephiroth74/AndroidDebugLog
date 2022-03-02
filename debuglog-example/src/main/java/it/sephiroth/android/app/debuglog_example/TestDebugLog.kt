package it.sephiroth.android.app.debuglog_example

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogClass
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogSkip
import it.sephiroth.android.library.asm.runtime.logging.Trunk
import timber.log.Timber
import java.io.InputStream
import java.io.StringBufferInputStream
import java.util.*


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 01.03.22 - 08:15
 */
@SuppressLint("LogNotTimber")
@DebugLogClass(logLevel = Log.INFO, debugArguments = DebugArguments.FULL, debugEnter = false, debugExit = true)
class TestDebugLog {

    init {
        Log.wtf(TestDebugLog::class.java.simpleName, "TestDebugLog::init")
    }

    @DebugLog(logLevel = Log.VERBOSE, debugArguments = DebugArguments.SHORT)
    fun test(context: Context) {
        val input = test1()
        val input2 = test2(input)
        val input3 = test3(input2)
        val input4 = test4(input3)
        test5(input3)
        test6(input3)
        test7(BuildConfig.VERSION_NAME)
        test8()
        test09(listOf("hello", "logged", "world"), context, intArrayOf(1, 2, 3))
        test10()
        test11()
    }

    @DebugLogSkip
    private fun test1(): Long {
        return System.currentTimeMillis()
    }

    @DebugLog(debugExit = false)
    private fun test2(input: Long): Pair<Long, String> {
        return Pair(input, UUID.randomUUID().toString())
    }

    private fun test3(input: Pair<Long, String>): Array<Any> {
        return arrayOf(input.first, input.second)
    }

    @DebugLog(logLevel = Log.VERBOSE)
    private fun test4(input: Array<Any>): List<String> {
        return input.map { it.toString() }.toList()
    }

    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.SHORT, debugEnter = true)
    private fun test5(input: Array<Any>): List<String> {
        return input.map { it.toString() }.toList()
    }

    @DebugLog(logLevel = Log.WARN, debugArguments = DebugArguments.NONE)
    private fun test6(input: Array<Any>): List<String> {
        return input.map { it.toString() }.toList()
    }


    fun test11() {
        Trunk.wtf("$this = testLog")
        Trunk.e("$this = testLog")
        Trunk.i("$this = testLog")
        Trunk.d("$this = testLog")
        Trunk.v("$this = testLog")
        Timber.v("[timber] $this = testLog")

        fun anonymous() {
            Timber.d("[timber] anonymous: %d", System.currentTimeMillis())
        }

        anonymous()
    }

    private fun test7(version: String?): Int {
        testReturnNull({})
        return BuildConfig.VERSION_CODE
    }

    private fun test8() {
        if (true) {
            return
        }
    }

    private fun test09(list: List<String?>?, context: Context, input: IntArray): Context {
        list?.forEach {
            Log.v(this::class.java.simpleName, "android.util.Log item=$it")
        }
        return context
    }

    private fun testReturnNull(type: Runnable?): Runnable? {
        val newType = type
        if (newType != null) {
            return Runnable { type.run() }
        }
        return null
    }

    private fun test10() {
        StaticInnerTestClass1().test()
        StaticInnerTestClass2().test()
        InnerTestClass3().apply { helloInnerTest() }
    }

    @DebugLogClass(logLevel = Log.WARN, debugArguments = DebugArguments.NONE, debugExit = false)
    class StaticInnerTestClass1 {

        init {
            Timber.i(this::class.java.name)
        }

        fun test() {
            Timber.v("test")
            try {
                test01()
            } catch (ignored: Exception) {
            }
            test02(listOf("Ciao", "mondo"), 666)
            test03()
            test04(1)
        }

        fun test01() {
            Timber.v("test01")
            throw IllegalStateException("test exception")
        }

        fun test02(input: List<String>, output: Int): InputStream {
            Timber.v("test02")
            return StringBufferInputStream(input.fold("") { sum, element -> sum + element }.toString() + "_" + BuildConfig.VERSION_NAME + "_" + output)
        }

        @DebugLogSkip
        fun test03() {
            Timber.v("test03")
        }

        fun test04(param1: Int): Boolean {
            Timber.v("test04")
            return param1 == 0
        }
    }

    class StaticInnerTestClass2 {

        init {
            Timber.i(this::class.java.name)
        }

        @DebugLog
        fun test() {
            Timber.v("test")
            try {
                test01()
            } catch (ignored: Exception) {
            }
            test02(listOf("Ciao", "mondo"), 666)
            test03()
            test04(1)
        }

        @DebugLog
        fun test01() {
            Timber.v("test01")
            throw IllegalStateException("test exception")
        }

        @DebugLog
        fun test02(input: List<String>, output: Int): InputStream {
            Timber.v("test02")
            return StringBufferInputStream(input.fold("") { sum, element -> sum + element }.toString() + "_" + BuildConfig.VERSION_NAME + "_" + output)
        }

        @DebugLogSkip
        fun test03() {
            Timber.v("test03")
        }

        @DebugLog
        fun test04(param1: Int): Boolean {
            Timber.v("test04")
            return param1 == 0
        }
    }

    inner class InnerTestClass3 {
        fun helloInnerTest() {
        }
    }
}
