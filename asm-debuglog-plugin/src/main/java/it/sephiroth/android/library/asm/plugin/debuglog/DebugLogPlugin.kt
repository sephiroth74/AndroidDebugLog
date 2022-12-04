package it.sephiroth.android.library.asm.plugin.debuglog

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.plugin.AsmBaseExtension
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor

abstract class DebugLogPlugin : Plugin<Project> {
    private lateinit var logger: Logger
    private val extensionName = BuildConfig.EXTENSION_NAME

    override fun apply(target: Project) {
        logger = target.logger
        logger.info("[DebugLogPlugin] apply(${target.name})")

        val base = if (target.extensions.findByName(Constants.BASE_EXTENSION_NAME) == null) {
            target.extensions.create(Constants.BASE_EXTENSION_NAME, AsmBaseExtension::class.java)
        } else {
            target.extensions.getByType(AsmBaseExtension::class.java)
        } // androidASM

        val extension = base.extensions.create(extensionName, DebugLogPluginExtension::class.java) // debugLog

        val androidComponent = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponent.onVariants { variant ->
            val runVariant = extension.runVariant
            val pattern = Regex(runVariant, RegexOption.IGNORE_CASE)
            val enabled = extension.enabled

            if (enabled) {
                if (variant.name.matches(pattern)) {
                    variant.instrumentation.transformClassesWith(ClassVisitorFactory::class.java, InstrumentationScope.PROJECT) { params ->
                        params.logLevel.set(extension.logLevel)
                        params.debugArguments.set(extension.debugArguments)
                        params.debugEnter.set(true)
                        params.debugExit.set(extension.debugExit)
                        params.verbose.set(extension.verbose)
                    }
                    variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
                } else {
                    logger.info("[$extensionName] ${target.name}:${variant.name} not enabled because runVariant[$runVariant] does not match")
                }
            } else {
                logger.info("[$extensionName] plugin not enabled on ${target.name}:${variant.name}")
            }
        }
    }

    abstract class ClassVisitorFactory : AsmClassVisitorFactory<DebugLogPluginParameters> {

        override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
            return PreClassVisitor(nextClassVisitor, classContext, parameters.get())
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
