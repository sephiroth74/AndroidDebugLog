// debuglog-annotations/build.gradle.kts

@file:Suppress("LocalVariableName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.signing.Sign

plugins {
    id("com.android.library")
    id("maven-publish")
    id("kotlin-android")
    signing
}

android {
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    compileOptions {
        sourceCompatibility = Config.Java.version
        targetCompatibility = Config.Java.version
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = Config.Kotlin.jvmVersion
    }
}

afterEvaluate {

    if (project.hasProperty("sonatypeUsername")
            && project.hasProperty("sonatypePassword")
            && project.hasProperty("SONATYPE_RELEASE_URL")
            && project.hasProperty("SONATYPE_SNAPSHOT_URL")
    ) {
        val SONATYPE_RELEASE_URL: String by project
        val SONATYPE_SNAPSHOT_URL: String by project
        val publishingUrl = if (!Config.DEBUG) SONATYPE_RELEASE_URL else SONATYPE_SNAPSHOT_URL


        publishing {
            publications {
                create<MavenPublication>("release") {

                    pom {
                        groupId = Config.GROUP
                        version = Config.VERSION

                        description.set(Config.Pom.DESCRIPTION)
                        url.set(Config.Pom.URL)
                        name.set(project.name)

                        from(components["release"])

                        licenses {
                            license {
                                name.set(Config.Pom.LICENCE_NAME)
                                url.set(Config.Pom.LICENCE_URL)
                            }
                        }

                        scm {
                            url.set(Config.Pom.SCM_URL)
                            connection.set(Config.Pom.SCM_CONNECTION)
                            developerConnection.set(Config.Pom.SCM_DEV_CONNECTION)
                        }

                        developers {
                            developer {
                                id.set(Config.Pom.DEVELOPER_ID)
                                name.set(Config.Pom.DEVELOPER_NAME)
                            }
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "sonatype"
                    url = uri(publishingUrl)
                    credentials {
                        val sonatypeUsername: String by project
                        val sonatypePassword: String by project

                        username = sonatypeUsername
                        password = sonatypePassword
                    }
                }
            }
        }

    }

    signing {
        sign(publishing.publications["release"])
    }

    tasks.withType<Sign> {
        onlyIf { !Config.DEBUG }
    }

    java {
        // empty
    }
}
dependencies {
    implementation(Config.Dependencies.Android.androidCoreKtx)
    implementation(kotlin(Config.Dependencies.JetBrains.stdLib))
}
repositories {
    mavenCentral()
}
