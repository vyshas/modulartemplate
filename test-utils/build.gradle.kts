plugins {
    id("android.library")
    id("kotlin.android")
}

android {
    namespace = "com.example.testutils"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    api(project(":core"))
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.kotlin.test)
}