// debuglog-annotations/build.gradle.kts

@file:Suppress("LocalVariableName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.signing.Sign

plugins {
    id("java-library")
    id("maven-publish")
    id("kotlin")
    id("org.jetbrains.kotlin.kapt")
    id("com.android.lint")
    signing
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(Config.Kotlin.jvmVersion)) }
}

dependencies {
    compileOnly(Config.Dependencies.Lint.api)
    compileOnly(Config.Dependencies.Lint.checks)
    compileOnly(Config.Dependencies.Auto.serviceAnnotations)
    kapt(Config.Dependencies.Auto.service)
    testImplementation(Config.Dependencies.Lint.core)
    testImplementation(Config.Dependencies.Lint.tests)
    testImplementation(Config.Dependencies.junit)

}
