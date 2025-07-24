plugins {
    id("pluggo.feature.api")
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
            java.srcDir("src/mock/java")
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(libs.androidx.navigation.compose)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.paging.common.android)
}
