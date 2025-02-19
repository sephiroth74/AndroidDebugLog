
plugins {
    id("com.dorongold.task-tree") version ("2.1.0") apply true
    id("com.android.application") version(Config.Versions.androidGradlePlugin) apply(false)
    id("com.android.library") version(Config.Versions.androidGradlePlugin) apply(false)
    id("org.jetbrains.kotlin.android") version(Config.KOTLIN_VERSION) apply(false)
}

