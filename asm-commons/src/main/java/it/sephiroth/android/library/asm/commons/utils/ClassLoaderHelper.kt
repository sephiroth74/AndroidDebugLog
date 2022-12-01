@file:Suppress("DEPRECATION")

package it.sephiroth.android.library.asm.commons.utils

import com.android.build.api.transform.TransformInput
import com.android.build.gradle.AppExtension
import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import org.gradle.api.Project
import java.io.File
import java.net.URL
import java.net.URLClassLoader

/**
 * Created by quinn on 31/08/2018
 */
object ClassLoaderHelper {

    @Suppress("DEPRECATION")
    fun getClassPathImports(inputs: Collection<TransformInput>, referencedInputs: Collection<TransformInput>, project: Project): List<URL> {
        val urls: ImmutableList.Builder<URL> = ImmutableList.Builder()
        val androidJarPath = getAndroidJarPath(project)
        val file = File(androidJarPath)
        val androidJarURL = file.toURI().toURL()
        urls.add(androidJarURL)

        Iterables.concat(inputs, referencedInputs).forEach { totalInputs ->
            totalInputs.directoryInputs.forEach { directoryInput ->
                if (directoryInput.file.isDirectory) {
                    urls.add(directoryInput.file.toURI().toURL())
                }
            }
            totalInputs.jarInputs.forEach { jarInput ->
                if (jarInput.file.isFile) {
                    urls.add(jarInput.file.toURI().toURL())
                }
            }
        }
        return urls.build()
    }

    @Suppress("DEPRECATION")
    fun getClassLoader(inputs: Collection<TransformInput>, referencedInputs: Collection<TransformInput>, project: Project): URLClassLoader {
        val classLoaderUrls = getClassPathImports(inputs, referencedInputs, project).toTypedArray()
        return URLClassLoader(classLoaderUrls)
    }

    private fun getAndroidJarPath(project: Project): String {
        val appExtension = project.properties["android"] as AppExtension
        var sdkDirectory = appExtension.sdkDirectory.absolutePath
        val compileSdkVersion = appExtension.compileSdkVersion
        sdkDirectory = sdkDirectory + File.separator + "platforms" + File.separator
        return sdkDirectory + compileSdkVersion + File.separator + "android.jar"
    }


}
