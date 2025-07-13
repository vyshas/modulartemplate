pluginManagement {
    includeBuild("build-logic")
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
include(":feature:order:wiring")
include(":core")
include(":feature:home:demo")
include(
    ":feature:product:api",
    ":feature:product:impl",
    ":feature:product:wiring",
    ":feature:product:demo"
)
include(":test-utils")
