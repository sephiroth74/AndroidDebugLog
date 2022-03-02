package it.sephiroth.android.app.debuglog_example

import android.content.Context
import android.util.Log
import it.sephiroth.android.library.asm.runtime.logging.Trunk
import timber.log.Timber
import java.io.InputStream
import java.io.StringBufferInputStream

class TestClass {

    fun test(context: Context) {
        Timber.v("[timber] test")
        onTestReturnInt(BuildConfig.VERSION_NAME)
        testVoidNoParams()
        testComplexParams(listOf("hello", "logged", "world"), context, intArrayOf(1, 2, 3))
        testInnerClass()
        testLog()
    }

    fun testLog() {
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

    private fun onTestReturnInt(version: String?): Int {
        testReturnNull({})
        return BuildConfig.VERSION_CODE
    }

    private fun testVoidNoParams() {
        if (true) {
            return
        }
    }

    private fun testComplexParams(list: List<String?>?, context: Context, input: IntArray): Context {
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

    private fun testInnerClass() {
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

    inner class InnerTestClass {
        fun helloInnerTest() {
        }
    }

}
