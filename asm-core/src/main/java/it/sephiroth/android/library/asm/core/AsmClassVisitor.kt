package it.sephiroth.android.library.asm.core

import it.sephiroth.android.library.asm.core.utils.StringUtils
import it.sephiroth.android.library.asm.core.vo.IPluginData
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

    var enabled = false
        protected set

    abstract fun secondPass(classWriter: AsmClassWriter, classReader: ClassReader)
}
