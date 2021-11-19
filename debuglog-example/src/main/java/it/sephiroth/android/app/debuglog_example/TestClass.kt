package it.sephiroth.android.app.debuglog_example

import android.content.Context
import android.util.Log
import it.sephiroth.android.library.debuglog.DebugArguments
import it.sephiroth.android.library.debuglog.DebugLog
import it.sephiroth.android.library.debuglog.DebugLogClass
import it.sephiroth.android.library.debuglog.MethodResultLogger
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.StringBufferInputStream

@DebugLogClass(debugArguments = DebugArguments.NONE, debugResult = true)
internal class TestClass {

    fun onTestReturnInt(version: String?): Int {
        return BuildConfig.VERSION_CODE
    }

    fun testVoidNoParams() {
    }

    fun testComplexParams(list: List<String?>?, context: Context): Context {
        return context
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

    class StaticInnerTestClass2() {

        fun test01(pair: Pair<Int, Boolean>, string: String): Pair<Int, String> {
            return Pair(pair.first, "$string${pair.second}")
        }

        fun test02() {

        }
    }

    inner class InnerTestClass() {
        fun helloInnerTest() {

        }
    }

}
