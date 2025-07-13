plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.testutils"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        maybeCreate("mock")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    api(project(":core"))
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.mockwebserver)
    api(libs.okio)
    api(libs.hamcrest)
    api(libs.retrofit)
    api(libs.retrofit.converter.moshi)
    api(libs.moshi.kotlin)
    api(libs.androidx.arch.core.testing)
}