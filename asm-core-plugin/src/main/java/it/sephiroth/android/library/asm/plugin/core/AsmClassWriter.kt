package it.sephiroth.android.library.asm.plugin.core

import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URLClassLoader

class AsmClassWriter(val className: String, private val superName: String, flags: Int, private val classLoader: URLClassLoader) : ClassWriter(flags) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java) as Logger

    override fun getCommonSuperClass(type1: String?, type2: String?): String {
        if (logger.isDebugEnabled) {
            logger.debug("Getting common super class. type1={}, type2={}", type1, type2)
        }

        val className = get(type1, type2)

        if (logger.isDebugEnabled) {
            logger.debug("Common super class is '{}'. type1={}, type2={}", className, type1, type2)
        }
        return className
    }

    private fun get(type1: String?, type2: String?): String {
        if (type1 == null || type1 == Constants.JavaTypes.TYPE_OBJECT || type2 == null || type2 == Constants.JavaTypes.TYPE_OBJECT) {
            // object is the root of the class hierarchy.
            return Constants.JavaTypes.TYPE_OBJECT
        }

        if (type1 == type2) {
            // two equal.
            return type1
        }

        // current class.
        if (type1 == className) {
            return getCommonSuperClass(superName, type2)
        } else if (type2 == className) {
            return getCommonSuperClass(type1, superName)
        }

        val type1ClassReader = getClassReader(type1)
        val type2ClassReader = getClassReader(type2)
        if (type1ClassReader == null || type2ClassReader == null) {
            logger.warn("Skip get common super class. not found class {type1={}, reader={}}, {type2={}, reader={}}", type1, type1ClassReader, type2, type2ClassReader)
            return Constants.JavaTypes.TYPE_OBJECT
        }

        // interface.
        if (isInterface(type1ClassReader)) {
            var interfaceName = type1
            if (isImplements(interfaceName, type2ClassReader)) {
                return interfaceName
            }

            if (isInterface(type2ClassReader)) {
                interfaceName = type2
                if (isImplements(interfaceName, type1ClassReader)) {
                    return interfaceName
                }
            }
            return Constants.JavaTypes.TYPE_OBJECT
        }

        // interface.
        if (isInterface(type2ClassReader)) {
            if (isImplements(type2, type1ClassReader)) {
                return type2
            }
            return Constants.JavaTypes.TYPE_OBJECT
        }

        // class.
        val superClassNames = hashSetOf<String>()
        superClassNames.add(type1)
        superClassNames.add(type2)

        var type1SuperClassName = type1ClassReader.superName
        if (!superClassNames.add(type1SuperClassName)) {
            // find common superClass.
            return type1SuperClassName
        }

        var type2SuperClassName = type2ClassReader.superName
        if (!superClassNames.add(type2SuperClassName)) {
            // find common superClass.
            return type2SuperClassName
        }

        while (type1SuperClassName != null || type2SuperClassName != null) {
            if (type1SuperClassName != null) {
                type1SuperClassName = getSuperClassName(type1SuperClassName)
                if (type1SuperClassName != null) {
                    if (!superClassNames.add(type1SuperClassName)) {
                        return type1SuperClassName
                    }
                }
            }

            if (type2SuperClassName != null) {
                type2SuperClassName = getSuperClassName(type2SuperClassName)
                if (type2SuperClassName != null) {
                    if (!superClassNames.add(type2SuperClassName)) {
                        return type2SuperClassName
                    }
                }
            }
        }

        return Constants.JavaTypes.TYPE_OBJECT
    }

    private fun isInterface(classReader: ClassReader): Boolean {
        return (classReader.access and Opcodes.ACC_INTERFACE) != 0
    }

    private fun isImplements(interfaceName: String, classReader: ClassReader?): Boolean {
        var classInfo = classReader

        while (classInfo != null) {
            val interfaceNames = classInfo.interfaces

            if (interfaceNames.any { name -> name != null && name == interfaceName }) {
                return true
            }


            interfaceNames.forEach { name ->
                if (name != null) {
                    val interfaceInfo = getClassReader(name)
                    if (interfaceInfo != null) {
                        if (isImplements(interfaceName, interfaceInfo)) {
                            return true
                        }
                    }
                }
            }

            val superClassName = classInfo.superName
            if (superClassName == null || superClassName == Constants.JavaTypes.TYPE_OBJECT) {
                break
            }
            classInfo = getClassReader(superClassName)
        }
        return false
    }

    private fun getSuperClassName(className: String): String? {
        val classReader = getClassReader(className) ?: return null
        return classReader.superName
    }

    private fun getClassReader(className: String): ClassReader? {
        try {
            val ins = classLoader.getResourceAsStream("$className.class")
            if (ins != null) {
                return ClassReader(ins)
            }
        } catch (ignored: IOException) {
            // not found class.
        }
        return null
    }
}
