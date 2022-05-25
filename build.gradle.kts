buildscript {

    dependencies {
//        classpath("it.sephiroth.android.library.asm:asm-debuglog-plugin:${Config.VERSION}")
//        classpath("it.sephiroth.android.library.asm:asm-logging-plugin:${Config.VERSION}")
//        classpath("it.sephiroth.android.library.asm:asm-logging-level-plugin:${Config.VERSION}")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version(Config.Versions.androidGradlePlugin) apply(false)
    id("com.android.library") version(Config.Versions.androidGradlePlugin) apply(false)
    id("org.jetbrains.kotlin.android") version(Config.KOTLIN_VERSION) apply(false)
}
