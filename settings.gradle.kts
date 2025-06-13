pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyProject"
include(":app")
include(":feature:home:api", ":feature:home:impl", ":feature:home:wiring")
include(":feature:order:api", ":feature:order:impl")
include(":core")
include(":feature:home:demo")
include(":feature:order:wiring")
