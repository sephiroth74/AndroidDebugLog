package it.sephiroth.android.app.debuglog_example

import android.content.Context
import android.util.Log
import it.sephiroth.android.library.asm.annotations.debuglog.DebugArguments
import it.sephiroth.android.library.asm.annotations.debuglog.DebugLog
import it.sephiroth.android.app.debuglog_example.BuildConfig
import it.sephiroth.android.library.asm.annotations.debuglog.DebugLogClass
import timber.log.Timber
import java.io.InputStream
import java.io.StringBufferInputStream

@DebugLogClass(debugArguments = DebugArguments.FULL, debugResult = true, logLevel = Log.WARN)
class TestClass {
    fun testAll(context: Context) {
        Timber.v("testAll")
        val t1 = onTestReturnInt(BuildConfig.VERSION_NAME)
        val t2 = testVoidNoParams()
        val t3 = testComplexParams(listOf("hello", "logged", "world"), context, intArrayOf(1, 2, 3))
        val t4 = testInnerClass()
    }

    fun onTestReturnInt(version: String?): Int {
        testReturnNull({})
        return BuildConfig.VERSION_CODE
    }

    fun testVoidNoParams() {
        if (true) {
            return
        }
    }

    @DebugLog(logLevel = Log.ERROR, debugArguments = DebugArguments.NONE, tag = "alessandro")
    fun testComplexParams(list: List<String?>?, context: Context, input: IntArray): Context {
        list?.forEach {
            Log.v(this::class.java.simpleName, "item=$it")
        }
        return context
    }

    fun testReturnNull(type: Runnable?): Runnable? {
        val newType = type
        if (newType != null) {
            return Runnable { type.run() }
        }
        return null
    }

    fun testInnerClass() {
        StaticInnerTestClass().apply {
            try {
                test01()
            } catch (ignored: Exception) {
            }
            test02(listOf("Ciao", "mondo"), 666)
            test03()
            test04(1)
        }

        StaticInnerTestClass2().apply {
            test01(Pair(BuildConfig.VERSION_CODE, true), BuildConfig.VERSION_NAME)
            test02()
        }

        InnerTestClass().apply {
            helloInnerTest()
        }
    }

    class StaticInnerTestClass() {
        fun test01() {
            throw IllegalStateException("test exception")
        }

        fun test02(input: List<String>, output: Int): InputStream {
            return StringBufferInputStream(input.fold("") { sum, element -> sum + element }.toString() + "_" + BuildConfig.VERSION_NAME + "_" + output)
        }

        fun test03() {

        }

        fun test04(param1: Int): Boolean {
            return param1 == 0
        }
    }

    class StaticInnerTestClass2 {

        fun test01(pair: Pair<Int, Boolean>, string: String): Pair<Int, String> {
            return Pair(pair.first, "$string${pair.second}")
        }

        fun test02() {

        }
    }

    inner class InnerTestClass() {

        @DebugLog
        fun helloInnerTest() {

        }
    }

}
