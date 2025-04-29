plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

dependencies {
    implementation(project(":common-conventions"))
    implementation(libs.dependency.analysis.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("settingsConventions") {
            id = "io.github.technoir42.conventions.settings"
            implementationClass = "io.github.technoir42.conventions.settings.SettingsConventionPlugin"
        }
    }
}
