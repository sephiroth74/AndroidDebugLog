package it.sephiroth.android.library.asm.plugin.debuglog

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import it.sephiroth.android.library.asm.commons.AndroidLogLevel
import it.sephiroth.android.library.asm.commons.Constants
import it.sephiroth.android.library.asm.commons.Constants.ASM_VERSION
import it.sephiroth.android.library.asm.plugin.debuglog.asm.post.PostClassVisitor
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.DebugLogPluginParameters
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodData
import it.sephiroth.android.library.asm.plugin.debuglog.asm.vo.MethodParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.objectweb.asm.ClassVisitor

abstract class DebugLogPluginPost : Plugin<Project> {
    private lateinit var logger: Logger
    private val extensionName = BuildConfig.EXTENSION_NAME

    companion object {
        private lateinit var project: Project

        fun getObjectMapping(): HashMap<String, HashMap<String, Pair<MethodData, List<MethodParameter>>>> {
            println("getObjectMapping(${project.name})")
            val prePlugin: DebugLogPluginPre = project.plugins.getPlugin(DebugLogPluginPre::class.java)
            println("prePlugin = $prePlugin")
            println("prePlugin.mapping = ${prePlugin.getObjectMapping()}")
            return prePlugin.getObjectMapping()
        }
    }

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
                        params.buildType.set(variant.buildType)
                        params.variantName.set(variant.name)
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
            logger.lifecycle("[post] createClassVisitor(${classContext.currentClassData.className})")

            val className = classContext.currentClassData.className
            val mapping = getObjectMapping()
            println("mapping = $mapping")

            return PostClassVisitor(nextClassVisitor, classContext) { getObjectMapping()[className]!! }

//            val baseDir = File("build/tmp/kotlin-classes/$flavorName")
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

}
