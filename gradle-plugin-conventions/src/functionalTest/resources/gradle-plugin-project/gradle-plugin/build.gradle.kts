plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "com.example.gradle.plugin"
}

gradlePlugin {
    plugins {
        register("example") {
            id = "com.example.gradle-plugin"
            implementationClass = "com.example.gradle.plugin.GradlePlugin"
        }
    }
}
