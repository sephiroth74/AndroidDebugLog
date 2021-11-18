// build.gradle.kts

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("file://" + File(System.getProperty("user.home"), ".m2/repository").absolutePath) }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
    }

    dependencies {
        classpath(Config.Dependencies.Android.buildGradle)
        classpath(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))

        // using remote repo
        classpath("it.sephiroth.android.library.debuglog:debuglog-plugin:${Config.VERSION}")
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("file://" + File(System.getProperty("user.home"), ".m2/repository").absolutePath) }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
    }
}
