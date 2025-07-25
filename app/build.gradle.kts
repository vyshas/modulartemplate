plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}


android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "GLOBAL_BASE_URL", "\"https://fakestoreapi.com/\"")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("Boolean", "IS_MOCK", "false")
        }
        getByName("release") {
            buildConfigField("Boolean", "IS_MOCK", "false")
        }
        create("mock") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            buildConfigField("Boolean", "IS_MOCK", "true")
            matchingFallbacks += listOf("debug")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
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
}

dependencies {
    
    implementation(project(":feature:home:api"))
    implementation(project(":feature:home:wiring"))
    implementation(project(":feature:order:api"))
    implementation(project(":feature:order:wiring"))

    // Core utilities
    implementation(project(":core"))

    implementation(libs.androidx.core)
    implementation(libs.kotlin.stdlib)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Hilt
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
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}