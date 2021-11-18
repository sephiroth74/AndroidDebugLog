package it.sephiroth.android.app.debuglog_example

import android.content.Context
import android.util.Log
import it.sephiroth.android.library.debuglog.DebugArguments
import it.sephiroth.android.library.debuglog.DebugLog
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.StringBufferInputStream

internal class TestClass {
    @DebugLog(debugArguments = DebugArguments.FULL)
    fun onTestReturnInt(version: String?): Int {
        return BuildConfig.VERSION_CODE
    }

    @DebugLog(logLevel = Log.ASSERT, debugResult = true)
    fun testVoidNoParams() {
    }

    @DebugLog(debugArguments = DebugArguments.SHORT, logLevel = Log.WARN, debugResult = true)
    fun testComplexParams(list: List<String?>?, context: Context): Context {
        return context
    }

    @DebugLog(debugResult = false)
    fun testInnerClass() {
        InnerTestClass().apply {
            try {
                test01()
            } catch (ignored: Exception) {
            }
            test02(listOf("Ciao", "mondo"), 666)
            test03()
        }
    }

    class InnerTestClass() {
        @DebugLog(logLevel = Log.VERBOSE, debugResult = true, debugArguments = DebugArguments.FULL, enabled = true)
        fun test01() {
            throw IllegalStateException("test exception")
        }

        @DebugLog(debugArguments = DebugArguments.SHORT)
        fun test02(input: List<String>, output: Int): InputStream {
            return StringBufferInputStream(input.fold("") { sum, element -> sum + element }.toString() + "_" + BuildConfig.VERSION_NAME + "_" + output)
        }

        @DebugLog(enabled = false)
        fun test03() {

        }
    }
}
