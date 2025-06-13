pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.squareup.anvil") version "2.4.6" apply false
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
include(":feature:home:api", ":feature:home:impl")
include(":feature:order:api", ":feature:order:impl")
include(":core")
include(":feature:home:demo")
