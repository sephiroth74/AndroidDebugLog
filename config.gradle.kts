project.extra.apply {
    val POM_VERSION: String by project
    val GROUP: String by project
    val kotlin_version: String by project

    set("kotlin_version", kotlin_version)
    set("version", POM_VERSION)
    set("group", GROUP)
    set("is_release", POM_VERSION.endsWith("SNAPSHOT"))

}
