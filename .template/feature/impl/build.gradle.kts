plugins {
    id("pluggo.feature.impl")
}

android {
    namespace = "com.example.feature.templatefeature.impl"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        maybeCreate("mock")
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
    implementation(project(":feature:templatefeature:api"))
    implementation(project(":core"))

    testImplementation(project(":test-utils"))
}