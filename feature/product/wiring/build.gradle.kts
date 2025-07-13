plugins {
    id("pluggo.feature.wiring")
}

android {
    buildTypes {
        maybeCreate("mock")
    }
    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("debug") {
            java.srcDirs("src/prod/java")
        }
        getByName("release") {
            java.srcDirs("src/prod/java")
        }
        maybeCreate("mock").apply {
            java.srcDirs("src/mock/java")
        }
    }
}

dependencies {
    api(project(":feature:product:api"))
    api(project(":feature:product:impl"))
    implementation(project(":core"))
}