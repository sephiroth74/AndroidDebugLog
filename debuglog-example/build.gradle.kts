// debuglog-example/build.gradle.kts

import it.sephiroth.android.library.debuglog.DebugArguments
import it.sephiroth.android.library.debuglog.AndroidLogLevel

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven { url = uri("file://" + File(System.getProperty("user.home"), ".m2/repository").absolutePath) }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
    }

    dependencies {
        classpath(Config.Dependencies.Android.buildGradle)
        classpath(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))
    }
}

plugins {
    id("com.android.application")
    id("kotlin-android")

    // when using remote repo
    id("it.sephiroth.android.library.debuglog")
}

// ---------  when using local buildSrc --------------
//apply<DebugLogPlugin>()
//
//configure<DebugLogPluginExtension> {
//    enabled.set(true)
//    logLevel.set(AndroidLogLevel.DEBUG)
//    debugResult.set(true)
//    debugArguments.set(DebugArguments.Full)
//    runVariant.set(Debug)
//}

// ---------- when using remote repo -----------------
androidDebugLog {
//    enabled.set(true)
    logLevel.set(AndroidLogLevel.VERBOSE)
    debugResult.set(false)
    debugArguments.set(DebugArguments.Full)
    runVariant.set(Regex(".*(debug)"))
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

    implementation("it.sephiroth.android.library.debuglog:debuglog-annotations:${Config.VERSION}")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
