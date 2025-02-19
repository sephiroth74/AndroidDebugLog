import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    groovy
    `java-gradle-plugin`
    `maven-publish`
    signing
    id("com.gradle.plugin-publish") version "1.3.1"
}

val artifactId = project.name
val groupId = Config.GROUP
val pluginId = "$groupId.$artifactId"

version = Config.VERSION
group = Config.GROUP

val SONATYPE_RELEASE_URL: String by project
val SONATYPE_SNAPSHOT_URL: String by project
val SONATYPE_TOKEN_USER: String by project
val SONATYPE_TOKEN_PASSWORD: String by project
val sonatypeUrl = if (!Config.DEBUG) SONATYPE_RELEASE_URL else SONATYPE_SNAPSHOT_URL

gradlePlugin {
    plugins {
        create("debugLogPlugin") {
            id = pluginId
            displayName = "Android Debug Log Plugin"
            description = "A compile time debug library annotation for android projects"
            vcsUrl = "https://github.com/sephiroth74/AndroidDebugLog"
            website = "https://github.com/sephiroth74/AndroidDebugLog"
            tags = listOf("ams", "android", "plugins")
            implementationClass = "it.sephiroth.android.library.asm.plugin.debuglog.DebugLogPlugin"
            version = version
        }
    }
}

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
            url = uri(sonatypeUrl)
            credentials {
                username = SONATYPE_TOKEN_USER
                password = SONATYPE_TOKEN_PASSWORD
            }
        }
        maven(ProjectUtil.artifactory(project))
        mavenLocal()
    }
}

signing {
    sign(publishing.publications["pluginMaven"])
}

tasks.withType<Sign> {
    onlyIf { !Config.DEBUG }
}



tasks.withType<KotlinCompile> {
    kotlinOptions {
        apiVersion = "2.1"
        languageVersion = "2.1"
        jvmTarget = "21"
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
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
