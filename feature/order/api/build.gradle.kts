plugins {
    id("pluggo.feature.api")
}

dependencies {
    implementation(project(":core"))

    // Expose Flow<T> in your APIs
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
}