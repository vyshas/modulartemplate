plugins {
    id("pluggo.feature.wiring")
}

dependencies {
    api(project(":feature:order:api"))
    api(project(":feature:order:impl"))
    implementation(project(":core"))
} 