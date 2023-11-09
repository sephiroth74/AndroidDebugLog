@file:Suppress("SpellCheckingInspection")

// debuglog-example/build.gradle.kts


plugins {
    id("com.android.application")
    id("kotlin-android")

    // include asm-debuglog plugin
    id("it.sephiroth.android.library.asm.asm-debuglog-plugin") version (Config.VERSION)

    // include ams-logging plugin
    id("it.sephiroth.android.library.asm.asm-logging-plugin") version (Config.VERSION)

}


/**
 * Main androidASM container
 * It will contain all the included plugins specific options
 */
androidASM {

    logging {
        enabled = true
        runVariant = ".*"
    }

    debugLog {
        enabled = true
        verbose.set(true)
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
        debug {
            buildConfigField("boolean", "IS_DEBUG", "true")
        }

        release {
            buildConfigField("boolean", "IS_DEBUG", "false")
        }
    }

    compileOptions {
        sourceCompatibility = Config.Java.version
        targetCompatibility = Config.Java.version
    }

    kotlinOptions {
        jvmTarget = Config.Kotlin.jvmVersion
    }

//    flavorDimensions += "api"
//
//    productFlavors {
//        this.create("flavor1") {
//            dimension = "api"
//        }
//    }


    sourceSets {
        getAt("release").java.srcDir("src/release/java")
        findByName("flavor1")?.kotlin?.srcDir("src/flavor1/java")
    }

}

dependencies {
    implementation(kotlin(Config.Dependencies.JetBrains.stdLib))

    // include asm runtime libs
    implementation(Config.Dependencies.AndroidAsm.debuglogRuntime)
    implementation(Config.Dependencies.AndroidAsm.loggingRuntime)
//    implementation(Config.Dependencies.AndroidAsm.loggingLevelRuntime)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("io.reactivex.rxjava3:rxjava:3.1.6")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
