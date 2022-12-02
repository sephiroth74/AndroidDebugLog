package it.sephiroth.android.library.asm.plugin.logging

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.Constants.BASE_EXTENSION_NAME
import it.sephiroth.android.library.asm.commons.plugin.AsmBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter


abstract class LoggingPlugin : Plugin<Project> {
    lateinit var logger: Logger
    private val extensionName = BuildConfig.EXTENSION_NAME

    override fun apply(target: Project) {
        logger = target.logger

        val base = if (target.extensions.findByName(BASE_EXTENSION_NAME) == null) {
            target.extensions.create(BASE_EXTENSION_NAME, AsmBaseExtension::class.java)
        } else {
            target.extensions.getByType(AsmBaseExtension::class.java)
        }

        val extension = base.extensions.create(extensionName, LoggingPluginExtension::class.java)
        val androidComponent = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponent.onVariants { variant ->
            val runVariant = extension.runVariant
            val pattern = Regex(runVariant, RegexOption.IGNORE_CASE)
            val enabled = extension.enabled

            if (enabled) {
                if (variant.name.matches(pattern)) {
                    variant.instrumentation.transformClassesWith(ClassVisitorFactory::class.java, InstrumentationScope.PROJECT) { params ->
                        params.writeToStdout.set(true)
                    }
                    variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
                } else {
                    logger.lifecycle("[$extensionName] ${target.name}:${variant.name} not enabled because runVariant[$runVariant] does not match")
                }
            } else {
                logger.lifecycle("[$extensionName] plugin not enabled on ${target.name}:${variant.name}")
            }
        }
    }

    interface ClassVisitorFactoryParams : InstrumentationParameters {
        @get:Input
        val writeToStdout: Property<Boolean>
    }

    abstract class ClassVisitorFactory : AsmClassVisitorFactory<ClassVisitorFactoryParams> {
        override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
            return LoggingClassVisitor(nextClassVisitor, classContext)
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
