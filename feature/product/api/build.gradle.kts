plugins {
    id("pluggo.feature.api")
}

dependencies {
    implementation(project(":core"))

    implementation(libs.androidx.navigation.compose)

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
}