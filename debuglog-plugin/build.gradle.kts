import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.signing.Sign

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("maven-publish")
    signing
    groovy
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    google()
}


configure<GradlePluginDevelopmentExtension> {
    plugins {
        create("androidDebugLog") {
            id = "it.sephiroth.android.library.debuglog"
            implementationClass = "it.sephiroth.android.library.debuglog.DebugLogPlugin"
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
            create<MavenPublication>("mavenJava") {
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

                    from(components["java"])

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
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign> {
    onlyIf { project.extra["is_release"] == true }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

afterEvaluate {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}
