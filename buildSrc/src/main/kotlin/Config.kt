import org.gradle.api.JavaVersion


/**
 * AndroidDebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 20:54
 */

object Config {
    const val ASM_VERSION = "9.2"

    const val KOTLIN_VERSION = "1.5.31"

    // this library version
    const val VERSION = "0.0.5-rc1-SNAPSHOT"

    const val GROUP = "it.sephiroth.android.library.asm"

    val DEBUG = VERSION.endsWith("SNAPSHOT")

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
        const val compileSdk = 31
        const val minSdk = 24
        const val targetSdk = 30
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
            const val buildGradle = "com.android.tools.build:gradle:7.0.3"
            const val buildGradleApi = "com.android.tools.build:gradle-api:7.0.3"
            const val androidCoreKtx = "androidx.core:core-ktx:1.7.0"
        }

        object Asm {
            const val asm = "org.ow2.asm:asm:${Config.ASM_VERSION}"
            const val asmUtil = "org.ow2.asm:asm-util:${Config.ASM_VERSION}"
            const val asmCommon = "org.ow2.asm:asm-commons:${Config.ASM_VERSION}"
        }

        object AndroidDebugLog {
            const val core = "$GROUP:asm-core:$VERSION"
            const val debugLog = "$GROUP:asm-debuglog:$VERSION"
            const val logging = "$GROUP:asm-logging:$VERSION"
        }
    }

}
