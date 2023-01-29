pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
}

rootProject.name = "AndroidDebugLog"

// runtime
include(":asm-debuglog-runtime")
include(":asm-logging-runtime")
include(":asm-logging-lint-runtime")

// plugins
include(":asm-commons")
include(":asm-debuglog-plugin")
include(":asm-logging-plugin")

// demo app
// include(":debuglog-example")
