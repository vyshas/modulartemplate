plugins {
    id("pluggo.feature.demo")
}

android {
    namespace = "com.example.feature.recipe.demo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.demo.recipe.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }

        getByName("release") {
            isMinifyEnabled = true
        }

        maybeCreate("mock").apply {
            initWith(getByName("debug"))
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            matchingFallbacks += listOf("debug")
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        kotlinCompilerExtensionVersion = libs.findVersion("compose").get().requiredVersion
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
    implementation(project(":feature:recipe:api"))
    implementation(project(":feature:recipe:wiring"))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}