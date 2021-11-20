# AndroidDebugLog

Originally forked from https://github.com/Leaking/Hunter and inspired by https://www.fatalerrors.org/a/how-to-use-transform-api-and-asm-to-implement-an-anti-quick-click-case.html.


# Installation

Root build.gradle file:

    buildscript {
        repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        ...
        classpath("it.sephiroth.android.library.debuglog:debuglog-plugin:*version*")
    }

}

Then, your module build.gradle file:

    plugins {
        id("com.android.application")
        id("kotlin-android")
        id("it.sephiroth.android.library.debuglog")
    }

    androidDebugLog {
        enabled.set(true)
        logLevel.set(AndroidLogLevel.VERBOSE)
        debugResult.set(false)
        debugArguments.set(DebugArguments.Full)
        runVariant.set(Debug)
    }

    
    dependencies {
        ...
        implementation("it.sephiroth.android.library.debuglog:debuglog-annotations:*version")
    }

# Usage

    @DebugLogClass
    class TestClass {
        ...class bidy
    }

or a single method:

    @Debug(logLevel=Log.INFO, debugArguments=DebugArguments.FULL, debugResult=true) // all annotation params are optional
    private fun hello(input: String) {
