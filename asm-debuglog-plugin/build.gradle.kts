// debuglog-plugin/build.gradle.kts

import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.plugins.signing.Sign

plugins {
    `kotlin-dsl`
    signing
    groovy
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.16.0"
}


version = Config.VERSION
group = Config.GROUP

pluginBundle {
    website = "https://github.com/sephiroth74/AndroidDebugLog"
    vcsUrl = "https://github.com/sephiroth74/AndroidDebugLog"
    tags = listOf("android", "annotations", "logging")
}


gradlePlugin {
    plugins {
        create("debugLogPlugin") {
            id = "it.sephiroth.android.library.asm.${project.name}"
            implementationClass = "it.sephiroth.android.library.asm.plugin.debuglog.DebugLogPlugin"
            displayName = "Android Debug Log Plugin"
            description = "A compile time debug library annotation for android projects"
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

//    api(project(":asm-core-plugin"))
    api(project(":asm-commons"))

    annotationProcessor(Config.Dependencies.Misc.lombok)
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
//        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        archiveClassifier.set("javadoc")
        dependsOn.add(javadoc)
        from(javadoc)
    }

    artifacts {
//        archives(sourcesJar)
//        archives(javadocJar)
        archives(jar)
    }
}

if (project.hasProperty("sonatypeUsername")
    && project.hasProperty("sonatypePassword")
    && project.hasProperty("SONATYPE_RELEASE_URL")
    && project.hasProperty("SONATYPE_SNAPSHOT_URL")
) {
    val SONATYPE_RELEASE_URL: String by project
    val SONATYPE_SNAPSHOT_URL: String by project
    val publishingUrl = if (Config.DEBUG == false) SONATYPE_RELEASE_URL else SONATYPE_SNAPSHOT_URL

    publishing {
        publications {
            create<MavenPublication>("pluginMaven") {
                groupId = Config.GROUP
                version = Config.VERSION

                pom {
                    groupId = Config.GROUP
                    version = Config.VERSION

                    description.set(Config.Pom.DESCRIPTION)
                    url.set(Config.Pom.URL)
                    name.set(project.name)


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

    signing {
        sign(publishing.publications["pluginMaven"])
    }

    tasks.withType<Sign> {
        onlyIf { !Config.DEBUG }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}


java {
    sourceCompatibility = Config.Java.version
    targetCompatibility = Config.Java.version
    withSourcesJar()
    withJavadocJar()
}

afterEvaluate {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = Config.Kotlin.jvmVersion
    }
}


// ------------ build config ----------------
