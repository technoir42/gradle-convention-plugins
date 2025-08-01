plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
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

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("gradlePluginConventions") {
            id = "io.github.technoir42.conventions.gradle-plugin"
            implementationClass = "io.github.technoir42.conventions.gradle.plugin.GradlePluginConventionPlugin"
        }
    }
}
