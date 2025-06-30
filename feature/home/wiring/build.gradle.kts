plugins {
    id("pluggo.feature.wiring")
}

dependencies {
    api(project(":feature:home:api"))
    api(project(":feature:home:impl"))
    implementation(project(":core"))
} 