import it.sephiroth.android.library.debuglog.*
import it.sephiroth.android.library.debuglog.RunVariant.*

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
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")
    }
}


plugins {
    id("com.android.application")
    id("kotlin-android")

    // when using remote repo
    // id("it.sephiroth.android.library.debuglog")
}

// ---------  when using local buildSrc --------------
apply<DebugLogPlugin>()

configure<DebugLogPluginExtension> {
    enabled.set(true)
    logLevel.set(AndroidLogLevel.DEBUG)
    debugResult.set(true)
    debugArguments.set(DebugArguments.Full)
    runVariant.set(Debug)
}

// ---------- when using remote repo -----------------
//androidDebugLog {
//    enabled.set(true)
//    logLevel.set(AndroidLogLevel.DEBUG)
//    debugResult.set(true)
//    debugArguments.set(DebugArguments.Full)
//    runVariant.set(Debug)
//}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "it.sephiroth.android.app.debuglog_example"
        minSdk = 24
        targetSdk = 31
        versionCode = 12345
        versionName = "1.2.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    val kotlin_version: String by project

    implementation(kotlin("stdlib-jdk8", version = kotlin_version))

    implementation("it.sephiroth.android.library.debuglog:debuglog-annotations:0.0.1-SNAPSHOT")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
