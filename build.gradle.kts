// build.gradle.kts

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://repo1.maven.org/maven2") }

    }

    dependencies {
        classpath(Config.Dependencies.Android.buildGradle)
        classpath(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))

//        classpath(Config.Dependencies.AndroidAsm.loggingPlugin)
//        classpath(Config.Dependencies.AndroidAsm.debugLogPlugin)
//        classpath(Config.Dependencies.AndroidAsm.loggingLevelPlugin)
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://repo1.maven.org/maven2") }
    }
}
