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
        maybeCreate("mock").apply {
            java.srcDirs("src/mock/java")
        }
    }
}

dependencies {
    implementation(project(":feature:templatefeature:api"))
    implementation(project(":feature:templatefeature:impl"))
    implementation(project(":core"))
}