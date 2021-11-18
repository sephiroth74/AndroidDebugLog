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
    compileSdk = 31

    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
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
        val publishingUrl = if (project.extra["is_release"] == true) SONATYPE_RELEASE_URL else SONATYPE_SNAPSHOT_URL


        publishing {
            publications {
                create<MavenPublication>("release") {

                    pom {
                        val POM_DESCRIPTION: String by project
                        val POM_URL: String by project
                        val POM_LICENCE_NAME: String by project
                        val POM_LICENCE_URL: String by project
                        val POM_SCM_URL: String by project
                        val POM_SCM_CONNECTION: String by project
                        val POM_SCM_DEV_CONNECTION: String by project
                        val POM_DEVELOPER_ID: String by project
                        val POM_DEVELOPER_NAME: String by project

                        groupId = project.ext["group"] as String
                        version = project.ext["version"] as String

                        description.set(POM_DESCRIPTION)
                        url.set(POM_URL)
                        name.set(project.name)

                        from(components["release"])

                        licenses {
                            license {
                                name.set(POM_LICENCE_NAME)
                                url.set(POM_LICENCE_URL)
                            }
                        }

                        scm {
                            url.set(POM_SCM_URL)
                            connection.set(POM_SCM_CONNECTION)
                            developerConnection.set(POM_SCM_DEV_CONNECTION)
                        }

                        developers {
                            developer {
                                id.set(POM_DEVELOPER_ID)
                                name.set(POM_DEVELOPER_NAME)
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
        onlyIf { project.extra["is_release"] == true }
    }

    java {
        // empty
    }
}
dependencies {
    val kotlin_version: String by project

    implementation("androidx.core:core-ktx:1.7.0")
    implementation(kotlin("stdlib-jdk8", version = kotlin_version))
}
repositories {
    mavenCentral()
}
