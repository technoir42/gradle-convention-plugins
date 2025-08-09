plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

dependencies {
    implementation(project(":common-conventions"))
    implementation(project(":gradle-extensions"))
    implementation(libs.dependency.analysis.gradle.plugin)
    implementation(libs.foojay.resolver.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("settingsConventions") {
            id = "io.github.technoir42.conventions.settings"
            implementationClass = "io.github.technoir42.conventions.settings.SettingsConventionPlugin"
        }
    }
}
