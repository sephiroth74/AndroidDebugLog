buildscript {
    apply(from = "${project.rootDir}/config.gradle.kts")

    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven {
            url = uri(
                "file://" + File(
                    System.getProperty("user.home"),
                    ".m2/repository"
                ).absolutePath
            )
        }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")

        // using remote repo
        // classpath("it.sephiroth.android.library.debuglog:debuglog-plugin:0.0.1-SNAPSHOT")
    }
}


allprojects {
    apply(from = "${project.rootDir}/config.gradle.kts")

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri(
                "file://" + File(
                    System.getProperty("user.home"),
                    ".m2/repository"
                ).absolutePath
            )
        }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
    }
}
