plugins {
    id("pluggo.feature.impl")
}

android {
    namespace = "com.example.feature.home.impl"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
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
    implementation(project(":feature:home:api"))
    implementation(project(":core"))
}