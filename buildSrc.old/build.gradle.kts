import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    google()
}

apply(plugin = "groovy")
apply(plugin = "java-library")


configure<GradlePluginDevelopmentExtension> {
    plugins {
        create("debugLogPlugin") {
            id = "it.sephiroth.android.library.debuglog"
            implementationClass = "it.sephiroth.android.library.debuglog.DebugLogPlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

afterEvaluate {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

dependencies {
    val asm_version: String by project
    val kotlin_version: String by project

    implementation(kotlin("stdlib-jdk8", version = kotlin_version))
    implementation(kotlin("reflect", version = kotlin_version))
    implementation(kotlin("gradle-plugin", version = kotlin_version))

    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("com.android.tools.build:gradle-api:7.0.3")

    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("commons-io:commons-io:2.6")
    implementation("org.ow2.asm:asm:${asm_version}")
    implementation("org.ow2.asm:asm-util:${asm_version}")
    implementation("org.ow2.asm:asm-commons:${asm_version}")

    implementation(gradleApi())
    implementation(localGroovy())

    annotationProcessor("org.projectlombok:lombok:1.18.12")
}
