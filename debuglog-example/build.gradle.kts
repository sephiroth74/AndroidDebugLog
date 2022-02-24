@file:Suppress("SpellCheckingInspection")

import it.sephiroth.android.library.asm.logginglevel.LoggingLevelPluginExtension

// debuglog-example/build.gradle.kts

//import it.sephiroth.android.library.debuglog.DebugArguments
//import it.sephiroth.android.library.debuglog.AndroidLogLevel

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://repo1.maven.org/maven2") }
    }

    dependencies {
        classpath(Config.Dependencies.Android.buildGradle)
        classpath(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))
    }
}

plugins {
    id("com.android.application")
    id("kotlin-android")

    // include asm-debuglog plugin
    id("it.sephiroth.android.library.asm.asm-debuglog")

    // include ams-logging-level plugin
    id("it.sephiroth.android.library.asm.asm-logging-level")
}

/**
 * Main androidASM container
 * It will contain all the included plugins specific options
 */
androidASM {

    debugLog {
        runVariant = ".*debug"
        debugResult = true
        debugArguments = it.sephiroth.android.library.asm.debuglog.DebugArguments.Full
        logLevel = it.sephiroth.android.library.asm.core.AndroidLogLevel.DEBUG
    }

    loggingLevel {
        minLogLevel = it.sephiroth.android.library.asm.core.AndroidLogLevel.ERROR
        includeLibs = true
    }
}


// ---------  when using local buildSrc --------------
//apply<it.sephiroth.android.library.debuglog.DebugLogPlugin>()
//
//configure<it.sephiroth.android.library.debuglog.DebugLogPluginExtension> {
//    enabled.set(true)
//    logLevel.set(it.sephiroth.android.library.debuglog.AndroidLogLevel.DEBUG)
//    debugResult.set(true)
//    debugArguments.set(it.sephiroth.android.library.debuglog.DebugArguments.Full)
//    runVariant.set(".*(debug)")
//}

// ---------- when using remote repo -----------------
//androidAsmDebugLog {
//    enabled.set(true)
//    logLevel.set(it.sephiroth.android.library.asm.core.AndroidLogLevel.VERBOSE)
//    debugResult.set(true)
//    debugArguments.set(it.sephiroth.android.library.asm.debuglog.DebugArguments.Full)
//    runVariant.set(".*")
//}

// ------------- Android ASM Logging Level ------------
//androidAsmLoggingLevel {
//    enabled.set(true)
//    minLogLevel.set(it.sephiroth.android.library.asm.core.AndroidLogLevel.INFO)
//    includeLibs.set(true)
//}

//androidDebugLogging {
//    minLogLevel.set(6)
//}

android {
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        applicationId = "it.sephiroth.android.app.debuglog_example"
        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk
        versionCode = 1
        versionName = "0.2.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = Config.Java.version
        targetCompatibility = Config.Java.version
    }

    kotlinOptions {
        jvmTarget = Config.Kotlin.jvmVersion
    }
}

dependencies {
    implementation(kotlin(Config.Dependencies.JetBrains.stdLib))

    // include asm-common lib
    implementation(Config.Dependencies.AndroidAsm.common)

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
