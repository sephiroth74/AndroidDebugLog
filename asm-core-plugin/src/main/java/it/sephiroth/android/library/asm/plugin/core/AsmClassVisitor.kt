package it.sephiroth.android.library.asm.plugin.core

import it.sephiroth.android.library.asm.plugin.core.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.core.vo.IPluginData
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.slf4j.LoggerFactory

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 16.11.21 - 15:34
 */
abstract class AsmClassVisitor(
    cv: AsmClassWriter,
    protected val className: String,
    protected val superName: String,
    protected val pluginData: IPluginData
) : ClassVisitor(Constants.ASM_VERSION, cv) {

    protected val simpleClassName = StringUtils.getSimpleClassName(className)

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger

    // set to true to allow the parent class visitor to call "secondPass" after the
    // first pass is done
    var requireSecondPass = false
        protected set

    abstract fun executeSecondPass(classWriter: AsmClassWriter, classReader: ClassReader)
}
