plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.github.technoir42.conventions.jvm"

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

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("jvmApplicationConventions") {
            id = "io.github.technoir42.conventions.jvm-application"
            implementationClass = "io.github.technoir42.conventions.jvm.JvmApplicationConventionPlugin"
        }
        register("jvmLibraryConventions") {
            id = "io.github.technoir42.conventions.jvm-library"
            implementationClass = "io.github.technoir42.conventions.jvm.JvmLibraryConventionPlugin"
        }
    }
}
