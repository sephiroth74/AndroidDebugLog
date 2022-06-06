# AndroidDebugLog

Provides bytecode injection at compile time by using the provided `DebugLog*` annotations. <br />
Once a class is annotated with the `@DebugLogClass` annotation, the final compiled class will log all the class methods. <br />
The annotation `@DebugLog` can be used to add logging to a single method.<br />
This gradle plugin doesn't use reflection to inject the logging. All the work is done at compile time.


Originally forked from https://github.com/Leaking/Hunter <br />
and inspired by https://www.fatalerrors.org/a/how-to-use-transform-api-and-asm-to-implement-an-anti-quick-click-case.html.

[![Maven Central](https://img.shields.io/maven-central/v/it.sephiroth.android.library.debuglog/debuglog-plugin)](https://repo1.maven.org/maven2/it/sephiroth/android/library/debuglog/debuglog-plugin/)

See last version here: https://search.maven.org/artifact/it.sephiroth.android.library.debuglog/debuglog-plugin


# Installation

Add the classpath `it.sephiroth.android.library.debuglog:debuglog-plugin:*version` to your root **build.gradle** file:


buildscript {
    ext.debuglog_version = "1.0.4"

    dependencies {
        ...
        classpath "it.sephiroth.android.library.asm:asm-debuglog-plugin:$debuglog_version"
        classpath "it.sephiroth.android.library.asm:asm-logging-plugin:$debuglog_version"
        classpath "it.sephiroth.android.library.asm:asm-logging-level-plugin:$debuglog_version"
    }
}


then in your root settings.gradle file:


    pluginManagement {
        repositories {
            gradlePluginPortal()
            google()
            mavenCentral()
            ...
            maven { url = uri("https://repo1.maven.org/maven2") }
        }
    }


Then, your in your module **build.gradle** file, enable the plugin:

    plugins {
        ...
        id 'it.sephiroth.android.library.asm.asm-debuglog-plugin'
        id 'it.sephiroth.android.library.asm.asm-logging-plugin'
        id 'it.sephiroth.android.library.asm.asm-logging-level-plugin'
    }

    
    // android asm plugins configuration block
    androidASM {

        // configuration for the Trunk logging
        logging {
            enabled = true
        }

        // configuration for DebugLog and DebugLogClass annotations
        debugLog {
            enabled = true
            debugExit = false
            debugArguments = it.sephiroth.android.library.asm.plugin.debuglog.DebugArguments.Full
            logLevel = it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel.DEBUG
        }

        // configuration for the logging level plugin
        loggingLevel {
            enabled = true
            runVariant = ".*release"
            minLogLevel = it.sephiroth.android.library.asm.plugin.core.AndroidLogLevel.DEBUG
            includeLibs = true
        }
    }

# Usage

You can add the `DebugLogClass` to the class declaration. In this way all the class methods will be logged

    @DebugLogClass
    class TestClass {
        ...
    }


Otherwise, to debug print a single method::

    @Debug(logLevel=Log.INFO, debugArguments=DebugArguments.FULL, debugResult=true) // all annotation params are optional
    private fun hello(input: String) {
        ...
    }


# License

    MIT License

    Copyright (c) 2021 Alessandro Crugnola

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
