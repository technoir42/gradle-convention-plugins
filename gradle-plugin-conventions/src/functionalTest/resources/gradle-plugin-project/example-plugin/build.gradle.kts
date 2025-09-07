plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePlugin {
    plugins {
        register("example") {
            id = "com.example.plugin"
            implementationClass = "com.example.plugin.ExamplePlugin"
        }
    }
}
