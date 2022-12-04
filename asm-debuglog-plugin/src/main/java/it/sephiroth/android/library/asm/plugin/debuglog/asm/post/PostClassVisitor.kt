package it.sephiroth.android.library.asm.plugin.debuglog.asm.post

import com.android.build.api.instrumentation.ClassContext
import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreMethodVisitor
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory

@Suppress("CanBeParameter")
class PostClassVisitor(
    private val classVisitor: ClassVisitor,
    private val classContext: ClassContext,
) : ClassVisitor(ASM_VERSION, classVisitor) {

    private val className: String = classContext.currentClassData.className
    private val simpleClassName = StringUtils.getSimpleClassName(className)
    private val logger: Logger = LoggerFactory.getLogger(PostClassVisitor::class.java) as Logger
    private val tag = Constants.makeTag(this)

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        logger.lifecycle("$tag visitMethod($simpleClassName:$name)")
//        val mv = InstructionAdapter(super.visitMethod(access, name, descriptor, signature, exceptions))
        return PostMethodVisitor(classContext, mv as PreMethodVisitor, access, name, descriptor)
    }

    override fun visitEnd() {
        super.visitEnd()
    }

}
