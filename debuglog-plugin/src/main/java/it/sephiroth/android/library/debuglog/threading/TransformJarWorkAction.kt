package it.sephiroth.android.library.debuglog.threading

import it.sephiroth.android.library.debuglog.DebugLogPlugin
import org.apache.commons.io.FileUtils
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logger
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.slf4j.LoggerFactory

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 17.11.21 - 13:46
 */
abstract class TransformJarWorkAction : WorkAction<TransformJarParams> {

    val logger: Logger = LoggerFactory.getLogger(DebugLogPlugin::class.java) as Logger

    override fun execute() {
        val srcJar = parameters.getSrcFile().get()
        val dstJar = parameters.getDstFile().get()

        logger.debug("[$TAG] transformJar(${srcJar.asFile.name} --> ${dstJar.asFile.name})")
        FileUtils.copyFile(srcJar.asFile, dstJar.asFile)
    }

    companion object {
        const val TAG = DebugLogPlugin.TAG
    }

}

interface TransformJarParams : WorkParameters {
    fun getSrcFile(): RegularFileProperty
    fun getDstFile(): RegularFileProperty
}
