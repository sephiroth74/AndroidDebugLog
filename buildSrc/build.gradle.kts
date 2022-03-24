plugins {
    `kotlin-dsl`
}


repositories {
    mavenCentral()
    mavenLocal()
    google()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases") }
}
