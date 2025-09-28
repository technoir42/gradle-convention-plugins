plugins {
    id("io.technoirlab.conventions.gradle-plugin")
}

gradlePluginConfig {
    buildFeatures {
        abiValidation = true
    }
}

dependencies {
    implementation(libs.dokka.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("rootConventions") {
            id = "io.technoirlab.conventions.root"
            implementationClass = "io.technoirlab.conventions.root.RootConventionPlugin"
        }
    }
}
