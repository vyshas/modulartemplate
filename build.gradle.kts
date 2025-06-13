// build.gradle.kts (project-level)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
