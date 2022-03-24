buildscript {
    // empty
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version(Config.Versions.androidGradlePlugin) apply(false)
    id("com.android.library") version(Config.Versions.androidGradlePlugin) apply(false)
    id("org.jetbrains.kotlin.android") version(Config.KOTLIN_VERSION) apply(false)
}

//// build.gradle.kts
//
//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//        mavenLocal()
//        maven { url = uri("https://plugins.gradle.org/m2/") }
//        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
//        maven { url = uri("https://repo1.maven.org/maven2") }
//
//    }
//
//    dependencies {
//        classpath(Config.Dependencies.Android.buildGradle)
//        classpath(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))
//
//        classpath(Config.Dependencies.AndroidAsm.loggingPlugin)
//        classpath(Config.Dependencies.AndroidAsm.debugLogPlugin)
//        classpath(Config.Dependencies.AndroidAsm.loggingLevelPlugin)
//    }
//}
//
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        mavenLocal()
//        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
//        maven { url = uri("https://repo1.maven.org/maven2") }
//    }
//}
