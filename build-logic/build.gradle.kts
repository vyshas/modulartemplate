
import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.8.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
}

gradlePlugin {
    plugins {
        register("pluggo.feature.api") {
            id = "pluggo.feature.api"
            implementationClass = "pluggo.feature.api.FeatureApiConventionPlugin"
        }
        register("pluggo.feature.demo") {
            id = "pluggo.feature.demo"
            implementationClass = "pluggo.feature.demo.FeatureDemoConventionPlugin"
        }
        register("pluggo.feature.impl") {
            id = "pluggo.feature.impl"
            implementationClass = "pluggo.feature.impl.FeatureImplConventionPlugin"
        }
        register("pluggo.feature.wiring") {
            id = "pluggo.feature.wiring"
            implementationClass = "pluggo.feature.wiring.FeatureWiringConventionPlugin"
        }
    }
}
