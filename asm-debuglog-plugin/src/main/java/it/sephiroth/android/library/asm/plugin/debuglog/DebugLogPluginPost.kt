package it.sephiroth.android.library.asm.plugin.debuglog

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.plugin.debuglog.asm.post.PostClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor

abstract class DebugLogPluginPost : Plugin<Project> {
    private lateinit var logger: Logger
    private lateinit var project: Project

    override fun apply(target: Project) {
        project = target
        logger = target.logger

        logger.lifecycle("[DebugLogPluginPost] apply(${target.name})")

        val androidComponent = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponent.onVariants { variant ->
            if (true) {
                if (true) {
                    variant.instrumentation.transformClassesWith(ClassVisitorFactory::class.java, InstrumentationScope.PROJECT) { params ->
                        params.logLevel.set(AndroidLogLevel.VERBOSE)
                        params.debugArguments.set(DebugArguments.Full)
                        params.debugEnter.set(true)
                        params.debugExit.set(true)
                    }
                    variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_ALL_CLASSES)
                }
            }
        }
    }

    abstract class ClassVisitorFactory : AsmClassVisitorFactory<DebugLogPluginParameters> {

        override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
            val className = classContext.currentClassData.className
            return PostClassVisitor(nextClassVisitor, classContext)
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            val result = when {
                classData.className.endsWith(".R") -> false
                classData.className.matches(".*\\.R\\$\\w+\$".toRegex()) -> false
                else -> true
            }
            return result
        }
    }
}
