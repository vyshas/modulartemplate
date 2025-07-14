package pluggo.feature.impl

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val pathSegments = project.path.split(":")
            if (pathSegments.size < 3 || pathSegments[1] != "feature") {
                throw IllegalArgumentException("This plugin should only be applied to feature modules with names like ':feature:featureName:impl'")
            }
            val featureName = pathSegments[2]

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
                apply("androidx.navigation.safeargs.kotlin")
            }

            extensions.configure<LibraryExtension> {
                namespace = "${project.group}.${project.path.replace(":", ".").substring(1)}"
                compileSdk = 35

                defaultConfig {
                    minSdk = 24
                }

                buildFeatures {
                    compose = true
                }

                val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

                composeOptions {
                    kotlinCompilerExtensionVersion =
                        libs.findVersion("compose").get().requiredVersion
                }

                (this as ExtensionAware).extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions>(
                    "kotlinOptions",
                ) {
                    jvmTarget = "17"
                }

                compileOptions {
                    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17
                    targetCompatibility = org.gradle.api.JavaVersion.VERSION_17
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("api", libs.findLibrary("kotlinx.coroutines.core").get())

                add("implementation", libs.findLibrary("androidx.core").get())
                add("implementation", libs.findLibrary("kotlin.stdlib").get())
                add("implementation", libs.findLibrary("compose.ui").get())
                add("implementation", libs.findLibrary("compose.material").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.fragment.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.fragment.ktx").get())

                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                add(
                    "implementation",
                    libs.findLibrary("androidx.lifecycle.viewmodel.compose").get(),
                )
                add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
                add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())

                add("implementation", libs.findLibrary("androidx.appcompat").get())
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("androidx.activity").get())
                add("implementation", libs.findLibrary("androidx.constraintlayout").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("implementation", libs.findLibrary("androidx.ui.graphics").get())
                add("implementation", libs.findLibrary("androidx.material3").get())

                add("implementation", libs.findLibrary("hilt.android").get())
                add("ksp", libs.findLibrary("hilt.compiler").get())

                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("mockk.android").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("turbine").get())

                // Network
                add("implementation", libs.findLibrary("retrofit").get())
                add("implementation", libs.findLibrary("retrofit.converter.moshi").get())
                add("implementation", libs.findLibrary("moshi").get())
                add("implementation", libs.findLibrary("moshi.kotlin").get())
                add("ksp", libs.findLibrary("moshi.codegen").get())
                add("implementation", libs.findLibrary("okhttp").get())
                add("implementation", libs.findLibrary("okhttp.logging.interceptor").get())
            }
        }
    }
}
