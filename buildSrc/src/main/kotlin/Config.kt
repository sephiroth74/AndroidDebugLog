@file:Suppress("SpellCheckingInspection")

import org.gradle.api.JavaVersion


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 20:54
 */

object Config {
    const val ASM_VERSION = "9.3"

    const val KOTLIN_VERSION = "1.9.0"

    // this library version
    const val VERSION = "2.0.8-rc2-SNAPSHOT"

    const val GROUP = "it.sephiroth.android.library.asm"

    val DEBUG = VERSION.endsWith("SNAPSHOT")

    object Versions {
        const val androidGradlePlugin = "7.4.1"
        const val androidTools = "32.0.0"
        const val autoService = "1.0.1"
    }

    object Pom {
        const val VERSION = Config.VERSION
        const val DESCRIPTION = "Android gradle plugin which inject at compile time method logging (initially forked from Hunter-Debug)"
        const val URL = "https://github.com/sephiroth74/AndroidDebugLog/"
        const val SCM_URL = "https://github.com/sephiroth74/AndroidDebugLog/"
        const val SCM_CONNECTION = "scm:git@github.com/sephiroth74/AndroidDebugLog.git"
        const val SCM_DEV_CONNECTION = "scm:git@github.com:sephiroth74/AndroidDebugLog.git"
        const val LICENCE_NAME = "MIT License"
        const val LICENCE_URL = "https://github.com/sephiroth74/AndroidDebugLog/blob/main/LICENSE"
        const val LICENCE_DIST = "repo"
        const val DEVELOPER_ID = "sephiroth74"
        const val DEVELOPER_NAME = "Alessandro Crugnola"
    }

    object Android {
        const val compileSdk = 33
        const val minSdk = 24
        const val targetSdk = 32
    }

    object Java {
        val version = JavaVersion.VERSION_11
    }

    object Kotlin {
        const val version = KOTLIN_VERSION
        const val jvmId = "jvm"
        const val kaptId = "kapt"
        const val jvmVersion = "11"
    }

    object MavenPublish {
        const val id = "maven-publish"
    }

    object Dependencies {
        // @formatter:off

        const val junit = "junit:junit:4.13.2"


        object Auto {
            const val service = "com.google.auto.service:auto-service:${Versions.autoService}"
            const val serviceAnnotations = "com.google.auto.service:auto-service-annotations:${Versions.autoService}"
        }

        object Lint {
            const val core = "com.android.tools.lint:lint:30.3.1"
            const val api = "com.android.tools.lint:lint-api:30.3.1"
            const val checks = "com.android.tools.lint:lint-checks:30.3.1"
            const val tests = "com.android.tools.lint:lint-tests:30.3.1"
        }

        object Misc {
            const val commonsIo = "commons-io:commons-io:2.6"
            const val guava = "com.google.guava:guava:31.0.1-jre"
            const val lombok = "org.projectlombok:lombok:1.18.12"
        }

        object JetBrains {
            const val kolinGradlePlugin = "gradle-plugin:$KOTLIN_VERSION"
            const val stdLib = "stdlib-jdk8:$KOTLIN_VERSION"
        }

        object Android {
            const val buildGradle = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
            const val buildGradleApi = "com.android.tools.build:gradle-api:${Versions.androidGradlePlugin}"
            const val androidCoreKtx = "androidx.core:core-ktx:1.7.0"
        }

        object Asm {
            const val asm = "org.ow2.asm:asm:$ASM_VERSION"
            const val asmUtil = "org.ow2.asm:asm-util:$ASM_VERSION"
            const val asmCommon = "org.ow2.asm:asm-commons:$ASM_VERSION"
        }

        object AndroidAsm {
            const val debuglogRuntime = "$GROUP:asm-debuglog-runtime:$VERSION"
            const val loggingRuntime = "$GROUP:asm-logging-runtime:$VERSION"
            const val loggingLevelRuntime = "$GROUP:asm-logging-level-runtime:$VERSION"

            const val corePlugin = "$GROUP:asm-core-plugin:$VERSION"
            const val debugLogPlugin = "$GROUP:asm-debuglog-plugin:$VERSION"
            const val loggingPlugin = "$GROUP:asm-logging-plugin:$VERSION"
            const val loggingLevelPlugin = "$GROUP:asm-logging-level-plugin:$VERSION"
            const val commons = "$GROUP:asm-commons:$VERSION"
        }

        // @formatter:on
    }

}
