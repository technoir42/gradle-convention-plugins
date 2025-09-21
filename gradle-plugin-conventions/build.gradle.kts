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
    implementation(project(":libraries:gradle-extensions"))
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin.api)

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(project(":libraries:gradle-test-kit"))
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
