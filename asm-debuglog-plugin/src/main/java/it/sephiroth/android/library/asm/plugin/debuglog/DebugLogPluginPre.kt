package it.sephiroth.android.library.asm.plugin.debuglog

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.commons.plugin.AsmBaseExtension
import it.sephiroth.android.library.asm.plugin.debuglog.asm.pre.PreClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.kotlin.dsl.getByType
import org.objectweb.asm.ClassVisitor

abstract class DebugLogPluginPre : Plugin<Project> {
    private lateinit var logger: Logger
    private val extensionName = BuildConfig.EXTENSION_NAME

    fun getObjectMapping() = map

    override fun apply(target: Project) {
        logger = target.logger
        logger.lifecycle("[DebugLogPluginPre] apply(${target.name})")


        val baseExtension = target.extensions.getByType(AsmBaseExtension::class.java)
        val extension = baseExtension.extensions.getByType(DebugLogPluginExtension::class.java)

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
                        params.buildType.set(variant.buildType)
                        params.variantName.set(variant.name)
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

    abstract class ClassVisitorFactory : AsmClassVisitorFactory<DebugLogPluginParameters> {

        override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
            logger.lifecycle("[pre] createClassVisitor(${classContext.currentClassData.className})")

            val loaded = classContext.loadClassData(classContext.currentClassData.className)
            println("loaded = $loaded")

            return PreClassVisitor(nextClassVisitor, classContext, parameters.get()) { clsName, clsMap ->
                map[clsName] = clsMap
            }

//            val baseDir = File("build/tmp/kotlin-classes/$flavorName")
//            val className = classContext.currentClassData.className
//            val classNameAsPath = className.replace(".", "/") + ".class"
//
//            val classFile = File(baseDir, classNameAsPath)
//
//            println("className: $className")
//
//            if (classFile.exists()) {
//                ClassReader(FileInputStream(classFile)).accept(
//                    PreClassVisitor(object : ClassVisitor(ASM_VERSION) {}, classContext, parameters.get()) { clsName, clsMap ->
//
//                        logger.lifecycle("$className will need a second pass!")
//                        map[clsName] = clsMap
//                    }, ClassReader.EXPAND_FRAMES
//                )
//
//                return map[className]?.let { classMethodsMap ->
//                    logger.lifecycle("Creating PostClassVisitor for $className")
//                    PostClassVisitor(classContext, nextClassVisitor, classMethodsMap)
//                } ?: run {
//                    object : ClassVisitor(ASM_VERSION, nextClassVisitor) {}
//                }
//            }


//            ClassReader(FileInputStream("/Users/alessandro/Documents/git/sephiroth74/AndroidDebugLog/debuglog-example/build/tmp/kotlin-classes/debug/it/sephiroth/android/app/debuglog_example/MainActivity.class"))
//                .accept(
//                    PreClassVisitor(nextClassVisitor, classContext, parameters.get()) { className: String, map: HashMap<String, Pair<MethodData, List<MethodParameter>>> ->
//                        DebugLogPluginPre.map[className] = map
//                    },
//                    ClassReader.EXPAND_FRAMES
//                )
//
//            return DebugLogPluginPre.map[classContext.currentClassData.className]?.let { paramsMap ->
//                return PostClassVisitor(classContext, nextClassVisitor, paramsMap)
//            } ?: run {
//                object : ClassVisitor(ASM_VERSION, nextClassVisitor) {}
//            }

            return object : ClassVisitor(ASM_VERSION, nextClassVisitor) {}

//            return PreClassVisitor(
//                nextClassVisitor,
//                classContext,
//                parameters.get(), null
//            )
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            val result = when {
                classData.className.endsWith(".R") -> false
                classData.className.matches(".*\\.R\\$\\w+\$".toRegex()) -> false
                else -> true
            }
            return result
        }

        companion object {
            private val logger = Logging.getLogger(Constants.BASE_EXTENSION_NAME)
//            val map = hashMapOf<String, HashMap<String, Pair<MethodData, List<MethodParameter>>>>()
        }
    }

    companion object {
        val map = hashMapOf<String, HashMap<String, Pair<MethodData, List<MethodParameter>>>>()
    }
}
