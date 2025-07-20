plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

dependencies {
    implementation(project(":gradle-extensions"))
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.serialization.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.buildconfig.gradle.plugin)
}
