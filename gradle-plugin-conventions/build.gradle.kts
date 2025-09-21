plugins {
    id("io.technoirlab.conventions.gradle-plugin")
}

gradlePluginConfig {
    buildFeatures {
        abiValidation = true
    }
}

dependencies {
    apiApi(project(":common-conventions")) {
        capabilities {
            requireCapability("${project.group}:common-conventions-api")
        }
    }
    implementation(project(":common-conventions"))
    implementation(project(":gradle-extensions"))
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.dokka.gradle.plugin)

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("gradlePluginConventions") {
            id = "io.technoirlab.conventions.gradle-plugin"
            implementationClass = "io.technoirlab.conventions.gradle.plugin.GradlePluginConventionPlugin"
        }
    }
}
