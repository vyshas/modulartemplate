package pluggo.feature.api

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val pathSegments = project.path.split(":")
            if (pathSegments.size < 3 || pathSegments[1] != "feature") {
                throw IllegalArgumentException("This plugin should only be applied to feature modules with names like ':feature:featureName:api'")
            }
            val featureName = pathSegments[2]

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                namespace = "${project.group}.${project.path.replace(":", ".").substring(1)}"
                compileSdk = 35

                defaultConfig {
                    minSdk = 24
                }

                compileOptions {
                    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17
                    targetCompatibility = org.gradle.api.JavaVersion.VERSION_17
                }

                (this as ExtensionAware).extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions>(
                    "kotlinOptions",
                ) {
                    jvmTarget = "17"
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // No common dependencies for API modules, as they should only define interfaces and data models.
            }
        }
    }
}
