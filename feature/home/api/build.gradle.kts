plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.feature.home.api"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":core"))

    // Expose Flow<T> in your APIs
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
}