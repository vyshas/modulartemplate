plugins {
    id("pluggo.feature.demo")
}

android {
    namespace = "com.example.feature.templatefeature.demo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.demo.templatefeature.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "GLOBAL_BASE_URL", "\"https://fakestoreapi.com/\"")
    }

    buildTypes {
        maybeCreate("mock").apply {
            initWith(getByName("debug"))
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            matchingFallbacks += listOf("debug")
        }
    }
    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            assets.srcDirs("src/main/assets")
        }
        maybeCreate("mock").apply {
            java.srcDirs("src/mock/java")
            assets.srcDirs("src/mock/assets")
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
    implementation(project(":feature:templatefeature:api"))
    implementation(project(":feature:templatefeature:wiring"))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}