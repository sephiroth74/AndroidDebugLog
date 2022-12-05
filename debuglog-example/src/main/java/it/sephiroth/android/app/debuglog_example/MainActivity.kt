package it.sephiroth.android.app.debuglog_example

//import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugArguments
//import it.sephiroth.android.library.asm.runtime.debuglog.annotations.DebugLog
//import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger
//import it.sephiroth.android.library.asm.runtime.debuglog.DebugLogger.DefaultDebugLogHandler
//import it.sephiroth.android.library.asm.runtime.logginglevel.LoggingLevel
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
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
    @NonNull
    final fun testMethod(@NonNull input: Int = 16): Int {
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

//        Trunk.w(
//            """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur ut lobortis eros. Donec fringilla nisl diam, id euismod massa facilisis vel. Maecenas vulputate sollicitudin velit in fringilla. Sed dignissim feugiat quam, sit amet tempor turpis malesuada sit amet. Praesent placerat a nisi id ornare. Phasellus lacus est, finibus ut dictum quis, dapibus eu justo. Donec et lorem at libero vestibulum vulputate. Maecenas tortor mauris, efficitur a vehicula eu, placerat eu metus. Quisque at ex venenatis, tristique turpis ac, scelerisque mauris. Nulla egestas sollicitudin vehicula. Proin et nisi vitae lectus pharetra placerat. Sed vel erat facilisis, lobortis ante vitae, tristique enim. In commodo est vel molestie varius. Curabitur gravida elementum finibus. Sed quam nibh, pulvinar eget iaculis nec, tincidunt sed turpis. Cras mattis varius sem sit amet congue. Sed elementum dapibus eros, ut tristique lorem sollicitudin sit amet. In aliquam nibh ac enim blandit semper. Quisque feugiat bibendum massa. Etiam accumsan odio augue, eget fermentum nulla venenatis molestie. Donec tempus at dui vel mollis. Etiam ac justo eu eros lobortis blandit quis non nunc. Aliquam vel diam porttitor, pellentesque orci eu, scelerisque quam. Fusce cursus lobortis magna, eu facilisis diam luctus non. Ut sit amet eros id nunc auctor iaculis. Vestibulum consequat velit auctor mauris venenatis aliquam. Nullam in ex consequat ipsum interdum auctor vitae at lectus. Aenean nunc magna, semper volutpat tempor a, maximus ut mauris. Maecenas eu quam volutpat, interdum odio id, suscipit mi. Duis vitae posuere eros. Etiam sapien est, finibus at placerat ornare, facilisis sed sapien. Aenean porttitor tellus in metus auctor, nec laoreet dolor posuere. Phasellus pulvinar facilisis sapien, a dignissim sapien malesuada in. Fusce pulvinar tincidunt quam nec tempor. Nulla convallis nec est ut euismod. Praesent ac nulla vitae tortor sagittis tincidunt sit amet quis purus. Duis vel varius purus, sit amet luctus sapien. In id mi lacinia, finibus elit at, porttitor dui. Etiam dictum elit lorem, sed pellentesque magna rutrum eu. Sed augue tortor, ullamcorper finibus tempus eu, rutrum nec ante. Vivamus eget velit diam. Donec sed pharetra mi, facilisis rutrum lectus. Integer et ex accumsan, pellentesque diam sit amet, fringilla mi. Etiam imperdiet libero vitae maximus egestas. Nulla id efficitur lectus. Maecenas sollicitudin nunc id arcu blandit, ut ultricies nisi efficitur. Donec ornare purus ullamcorper augue sodales laoreet. Mauris quis bibendum risus. Fusce ac est sed massa iaculis tempus. Aliquam sed bibendum ipsum, sed bibendum neque. In fringilla tempor sem, nec semper ex gravida non. Cras suscipit est posuere nisi finibus suscipit. Morbi ipsum orci, porttitor vitae eleifend ac, suscipit ut leo. Donec mauris lacus, molestie in lobortis id, lobortis et ipsum. Sed eu felis ac mauris commodo dignissim. Aliquam erat volutpat. Aenean et nunc ut mi commodo tincidunt. Sed rutrum neque nec sem porta mattis. Vivamus bibendum lorem nec lectus luctus dictum id ut purus. In pharetra sit amet diam ut malesuada. Nunc eget lacus id ante venenatis posuere. Phasellus faucibus ut lacus viverra posuere. Praesent elementum porta sollicitudin. Maecenas vitae pulvinar lectus. Nunc hendrerit mauris et quam accumsan, sed euismod nisl pretium. Pellentesque sed placerat enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Proin non sagittis quam. Maecenas congue erat ac congue congue. Nam sollicitudin tempor diam, in egestas odio scelerisque vitae. Aenean eu scelerisque libero. Interdum et malesuada fames ac ante ipsum primis in faucibus. Aenean finibus pretium orci sed sodales. Nunc in ultricies tortor, sit amet auctor ligula. Praesent rutrum ipsum ut orci commodo, vitae hendrerit nisl dignissim. Nam bibendum turpis id magna mollis tincidunt. Aliquam sit amet dui est. Quisque elit lacus, elementum malesuada purus vel, ultricies pretium turpis. Cras massa est, faucibus ut nibh id, cursus vestibulum tortor. Sed eu enim quis nibh aliquet volutpat non vitae orci. Duis at velit imperdiet, elementum elit quis, porttitor diam. Nam magna turpis, imperdiet ut lectus ut, euismod lobortis mi. Phasellus sed iaculis ante, id fringilla sapien. Aenean in bibendum nisi. Fusce pellentesque hendrerit nunc, sed lacinia est vehicula a. Integer eget malesuada sem, a elementum sapien. Phasellus dignissim leo sit amet libero accumsan, euismod sagittis ipsum hendrerit. Maecenas placerat odio id risus tempor mattis. Sed dapibus egestas nibh, a tempus leo fringilla nec. Suspendisse quam mi, ornare non elit ac, vestibulum ultricies augue. Pellentesque id est sit amet urna viverra euismod. Duis elit risus, iaculis bibendum ante a, consectetur porta ex. Curabitur congue, odio non facilisis accumsan, eros nisi efficitur mauris, non venenatis justo sapien sit amet nunc. Nam magna mi, ultrices in eros ac, rhoncus pretium urna. Sed iaculis ornare nibh, sit amet lobortis diam malesuada non. Donec nunc enim, sodales convallis tristique in, pellentesque a felis. Sed quis est porttitor, iaculis neque eget, tristique nisl. Vivamus rutrum, neque a venenatis maximus, est erat condimentum erat, a sagittis elit erat nec nibh. Aliquam volutpat augue sed enim ullamcorper, sit amet semper mi rhoncus. Mauris sagittis, tortor et feugiat efficitur, urna elit tincidunt arcu, in suscipit quam urna id quam. Phasellus rhoncus euismod turpis, ac porta ex sodales sit amet. Praesent dignissim egestas velit. Donec nec velit nec justo ultrices iaculis sed sed nisl. Phasellus in fringilla dolor. Nulla faucibus, justo sed condimentum aliquet, erat purus placerat mauris, a placerat sem ipsum sit amet sem. Maecenas at quam sed justo semper commodo in sit amet lectus. Suspendisse luctus risus ac ullamcorper pulvinar. Curabitur dolor dolor, tempus nec lacus in, interdum efficitur lectus. Integer interdum vestibulum erat, a imperdiet lacus fringilla in. Etiam fermentum mi eget felis tincidunt, vel ultricies nunc tincidunt. Morbi vehicula aliquam sem, id dictum eros mattis id. Maecenas dapibus commodo purus, bibendum tempor tellus accumsan non. Phasellus ut tincidunt nulla. Curabitur sapien metus, fringilla at nisl at, vulputate interdum magna. Aliquam erat volutpat. Vestibulum lobortis sed leo non blandit. Aliquam erat volutpat. Aenean viverra arcu et tempor condimentum. Fusce purus mauris, maximus sit amet congue sed, feugiat fermentum erat. In imperdiet egestas molestie. Nullam consectetur tincidunt ligula ac auctor. Sed semper mollis mauris, eu convallis magna mattis et. Integer non imperdiet sem. Aenean facilisis augue in ante pulvinar facilisis. Cras sagittis sem dui, ac malesuada quam pulvinar quis. Proin eget neque non odio porta placerat. Nullam porttitor fringilla lectus quis pretium. Cras vitae tellus leo. Vestibulum erat ipsum, consectetur auctor justo in, egestas aliquam ex. Aenean et felis libero. Maecenas vel semper urna, sit amet hendrerit libero. Maecenas vel imperdiet ex. Sed blandit mollis nibh, quis auctor tellus eleifend ut. Curabitur fringilla est eu odio lobortis gravida. In ut ligula dapibus tortor sodales convallis. Maecenas nec velit eget turpis posuere cursus in id mauris. Mauris finibus augue ultrices purus ornare aliquam. Aliquam nisl massa, malesuada sed sapien nec, fermentum bibendum ipsum. Donec elementum quam elit. Ut et iaculis lorem, at auctor quam. Sed mattis ligula venenatis diam blandit sagittis. Integer congue bibendum neque. Integer imperdiet erat nec odio tincidunt tempor. Integer a elementum ante, molestie rhoncus felis. Aenean malesuada venenatis ante, vel placerat eros euismod vitae. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec semper imperdiet venenatis. Phasellus bibendum turpis vel mauris placerat, et blandit arcu convallis. Vivamus ac augue vel libero scelerisque bibendum at sed ante. Ut dictum lorem vel urna commodo, ut porta lectus tristique. Quisque quis orci dapibus, elementum dui sed, efficitur ligula. Aliquam eu lobortis nulla, id elementum urna. Donec viverra id ante a fringilla. Maecenas commodo, dui sit amet tincidunt pharetra, felis nisi efficitur tellus, at auctor vivamus."""
//        )
    }

    //    @DebugLog(logLevel = Log.DEBUG, debugArguments = DebugArguments.FULL, debugExit = true, debugEnter = true, tag = "ciccio")
    override fun onResume() {
        super.onResume()
        runTests()
    }

    @SuppressLint("CheckResult")
    private fun runTests() {
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
