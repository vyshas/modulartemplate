plugins {
    id("pluggo.feature.wiring")
}

android {
    flavorDimensions += "mode"
    productFlavors {
        create("mock") {
            dimension = "mode"
        }
        create("prod") {
            dimension = "mode"
        }
    }
    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("mock") {
            java.srcDirs("src/mock/java")
        }
        getByName("prod") {
            java.srcDirs("src/prod/java")
        }
    }
}

dependencies {
    api(project(":feature:product:api"))
    api(project(":feature:product:impl"))
    implementation(project(":core"))
}