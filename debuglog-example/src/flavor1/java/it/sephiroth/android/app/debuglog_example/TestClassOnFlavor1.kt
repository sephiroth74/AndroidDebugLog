package it.sephiroth.android.app.debuglog_example

import android.util.Log
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments
import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog

class TestClassOnFlavor1 {

    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.FULL, debugExit = true, debugEnter = true, tag = "ciccio")
    fun onCreate() {
    }

    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.FULL, debugExit = true, debugEnter = true, tag = "ciccio")
    fun onResume(): Int {
        return 1
    }

    companion object {


        class InnerClass1 {
            @DebugLog
            fun test() {
            }
        }
    }

    class OuterClass {
        @DebugLog
        fun test() {
        }
    }

    inner class InnerClass2 {
        @DebugLog
        fun test() {
        }
    }
}
