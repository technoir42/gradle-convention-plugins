plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

dependencies {
    implementation(project(":common-conventions"))
    implementation(libs.kotlin.gradle.plugin.api)
}

gradlePlugin {
    plugins {
        register("gradlePluginConventions") {
            id = "io.github.technoir42.conventions.gradle-plugin"
            implementationClass = "io.github.technoir42.conventions.gradle.plugin.GradlePluginConventionPlugin"
        }
    }
}
