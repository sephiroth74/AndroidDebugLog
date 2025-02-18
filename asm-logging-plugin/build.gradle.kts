// debuglog-plugin/build.gradle.kts

@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    signing
    groovy
    `java-library`
    `java-gradle-plugin`
    java
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
}

version = Config.VERSION
group = Config.GROUP

val SONATYPE_RELEASE_URL: String by project
val SONATYPE_SNAPSHOT_URL: String by project

publishing {
    repositories {
        maven {
            name = "sonatypePluginRepository"
            url = if (!Config.DEBUG) uri(SONATYPE_RELEASE_URL) else uri(SONATYPE_SNAPSHOT_URL)
            credentials {
                val SONATYPE_TOKEN_USER: String by project
                val SONATYPE_TOKEN_PASSWORD: String by project

                username = SONATYPE_TOKEN_USER
                password = SONATYPE_TOKEN_PASSWORD
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("loggingPlugin") {
            id = "it.sephiroth.android.library.asm.asm-logging-plugin"
            displayName = "Log level plugin for AndroidASM"
            description = "Logging tag made at compile time"
            vcsUrl = "https://github.com/sephiroth74/AndroidDebugLog"
            website = "https://github.com/sephiroth74/AndroidDebugLog"
            tags = listOf("ams", "android", "plugins")
            implementationClass = "it.sephiroth.android.library.asm.plugin.logging.LoggingPlugin"
        }
    }
}


dependencies {
    implementation(kotlin(Config.Dependencies.JetBrains.stdLib))
    implementation(kotlin(Config.Dependencies.JetBrains.kolinGradlePlugin))
    implementation(Config.Dependencies.Android.buildGradle)
    implementation(Config.Dependencies.Android.buildGradleApi)

    implementation(Config.Dependencies.Misc.guava)
    implementation(Config.Dependencies.Misc.commonsIo)
    implementation(Config.Dependencies.Asm.asm)
    implementation(Config.Dependencies.Asm.asmUtil)
    implementation(Config.Dependencies.Asm.asmCommon)

    implementation(gradleApi())
    implementation(localGroovy())

    api(project(":asm-commons"))
}

tasks {
    artifacts {
        archives(jar)
    }
}
//
//if (project.hasProperty("SONATYPE_TOKEN_USER")
//    && project.hasProperty("SONATYPE_TOKEN_PASSWORD")
//    && project.hasProperty("SONATYPE_RELEASE_URL")
//    && project.hasProperty("SONATYPE_SNAPSHOT_URL")
//) {
//    val SONATYPE_RELEASE_URL: String by project
//    val SONATYPE_SNAPSHOT_URL: String by project
//    val publishingUrl = if (!Config.DEBUG) SONATYPE_RELEASE_URL else SONATYPE_SNAPSHOT_URL
//
//    publishing {
//        publications {
//            create<MavenPublication>("pluginMaven") {
//                groupId = Config.GROUP
//                version = Config.VERSION
//
//                pom {
//                    groupId = Config.GROUP
//                    version = Config.VERSION
//
//                    description.set(Config.Pom.DESCRIPTION)
//                    url.set(Config.Pom.URL)
//                    name.set(project.name)
//
//                    licenses {
//                        license {
//                            name.set(Config.Pom.LICENCE_NAME)
//                            url.set(Config.Pom.LICENCE_URL)
//                        }
//                    }
//
//                    scm {
//                        url.set(Config.Pom.SCM_URL)
//                        connection.set(Config.Pom.SCM_CONNECTION)
//                        developerConnection.set(Config.Pom.SCM_DEV_CONNECTION)
//                    }
//
//                    developers {
//                        developer {
//                            id.set(Config.Pom.DEVELOPER_ID)
//                            name.set(Config.Pom.DEVELOPER_NAME)
//                        }
//                    }
//                }
//            }
//        }
//
//        repositories {
//            maven {
//                name = "sonatype"
//                url = uri(publishingUrl)
//                credentials {
//                    val SONATYPE_TOKEN_USER: String by project
//                    val SONATYPE_TOKEN_PASSWORD: String by project
//
//                    username = SONATYPE_TOKEN_USER
//                    password = SONATYPE_TOKEN_PASSWORD
//                }
//            }
//        }
//
//
//    }
//
//    signing {
//        sign(publishing.publications["pluginMaven"])
//    }
//
//    tasks.withType<Sign> {
//        onlyIf { !Config.DEBUG }
//    }
//}

publishing {
    repositories {
        mavenLocal()
    }
}


java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(Config.Kotlin.jvmVersion)) }
    withSourcesJar()
    withJavadocJar()
}

afterEvaluate {
    tasks.withType<KotlinCompile> {
        compilerOptions {
            apiVersion.set(Config.Versions.kotlinApiVersion)
            jvmTarget.set(Config.Versions.jvmTarget)
            languageVersion.set(Config.Versions.kotlinLanguageVersion)
        }
    }
}


// ------------ build config ----------------
