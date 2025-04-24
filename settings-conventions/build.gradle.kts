plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePlugin {
    plugins {
        register("settingsConventions") {
            id = "io.github.technoir42.conventions.settings"
            implementationClass = "io.github.technoir42.conventions.settings.SettingsConventionPlugin"
        }
    }
}

dependencies {
    implementation(project(":common-conventions"))
}
