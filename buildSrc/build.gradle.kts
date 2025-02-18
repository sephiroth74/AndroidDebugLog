import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    groovy
    `java-gradle-plugin`
}


repositories {
    mavenCentral()
    mavenLocal()
    google()
    gradlePluginPortal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
}


dependencies {
    implementation("com.android.tools.build:gradle-api:8.8.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.1.10")
    implementation(gradleApi())
}

tasks.withType<KotlinCompile> {
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21

        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

