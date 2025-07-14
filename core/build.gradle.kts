plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        maybeCreate("mock")
    }
    sourceSets {
        maybeCreate("mock").apply {
            java.srcDir("src/mock/java")
        }
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.androidx.core)
    implementation(libs.kotlin.stdlib)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Network
    api(libs.retrofit)
    api(libs.retrofit.converter.moshi)
    api(libs.moshi)
    api(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
    implementation(libs.kotlin.reflect)

    // Logging
    api(libs.timber)
}