plugins {
    id("pluggo.feature.impl")
}

android {
    namespace = "com.example.feature.product.impl"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    flavorDimensions += "mode"
    productFlavors {
        create("mock") {
            dimension = "mode"
        }
        create("prod") {
            dimension = "mode"
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            assets.srcDirs("src/main/assets")
        }
        getByName("mock") {
            java.srcDirs("src/mock/java")
            assets.srcDirs("src/mock/assets")
        }
        getByName("prod") {
            java.srcDirs("src/prod/java")
            assets.srcDirs("src/prod/assets")
        }
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
    implementation(project(":feature:product:api"))
    implementation(project(":core"))

    testImplementation(project(":test-utils"))
}