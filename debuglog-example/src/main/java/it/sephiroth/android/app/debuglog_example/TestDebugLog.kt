package it.sephiroth.android.app.debuglog_example

import android.annotation.SuppressLint
import android.util.Log
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLogClass
import java.util.*


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 01.03.22 - 08:15
 */
@SuppressLint("LogNotTimber")
@DebugLogClass(debugResult = true, logLevel = Log.INFO, debugArguments = DebugArguments.FULL)
class TestDebugLog {

    init {
        Log.wtf(TestDebugLog::class.java.simpleName, "TestDebugLog::init")
    }

    fun test() {
        val input = test1()
        val input2 = test2(input)
        val input3 = test3(input2)
        val input4 = test4(input3)
        test5(input3)
        test6(input3)
    }

    private fun test1(): Long {
        return System.currentTimeMillis()
    }

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

    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.SHORT)
    private fun test5(input: Array<Any>): List<String> {
        return input.map { it.toString() }.toList()
    }

    @DebugLog(logLevel = Log.WARN, debugArguments = DebugArguments.NONE)
    private fun test6(input: Array<Any>): List<String> {
        return input.map { it.toString() }.toList()
    }
}
