package it.sephiroth.android.app.debuglog_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Uncomment these lines to
        // install a custom logger which will replace the pre-installed one
//        DebugLogger.installLog(object : DebugLogHandler() {
//            override fun logEnter(priority: Int, tag: String?, methodName: String?, params: String?) {
//                Log.i("[DEBUG_CUSTOM]", "---> $tag:$methodName($params)")
//            }
//
//            override fun logExit(priority: Int, tag: String?, methodName: String?, costedMillis: Long, result: String?) {
//                Log.i("[DEBUG_CUSTOM]", "<--- $tag:$methodName($result) in $costedMillis ms")
//            }
//        })
    }

    override fun onResume() {
        super.onResume()
        val testClass = TestClass()
        testClass.onTestReturnInt(BuildConfig.VERSION_NAME)
        testClass.testVoidNoParams()
        testClass.testComplexParams(listOf("hello", "logged", "world"), this)
        testClass.testInnerClass()
    }

}
