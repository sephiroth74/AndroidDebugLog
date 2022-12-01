@file:Suppress("SpellCheckingInspection")

// debuglog-example/build.gradle.kts

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
        classpath(Config.Dependencies.AndroidAsm.loggingPlugin)
    }
}

plugins {
    id("com.android.application")
    id("kotlin-android")

    // include asm-debuglog plugin
    id("it.sephiroth.android.library.asm.asm-debuglog-plugin")

    // include ams-logging plugin
    id("it.sephiroth.android.library.asm.asm-logging-plugin")

    // include ams-logging-level plugin (should be the last plugin inside the plugins block)
    id("it.sephiroth.android.library.asm.asm-logging-level-plugin")
}


/**
 * Main androidASM container
 * It will contain all the included plugins specific options
 */
androidASM {

    logging {
        enabled = true
        runVariant = ".*debug"
    }

    debugLog {
        enabled = true
        runVariant = ".*debug"
        debugExit = true
        debugArguments = it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments.Full
        logLevel = it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel.INFO
    }

    loggingLevel {
        enabled = false
        runVariant = ".*debug"
        minLogLevel = it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel.DEBUG
        includeLibs = true
    }
}




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

    sourceSets.getAt("release").java.srcDir("src/release/java")

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

    // include asm runtime libs
    implementation(Config.Dependencies.AndroidAsm.debuglogRuntime)
    implementation(Config.Dependencies.AndroidAsm.loggingRuntime)
    implementation(Config.Dependencies.AndroidAsm.loggingLevelRuntime)

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("io.reactivex.rxjava3:rxjava:3.1.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
