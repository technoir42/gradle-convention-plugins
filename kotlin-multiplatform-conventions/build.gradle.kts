plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.github.technoir42.conventions.kotlin.multiplatform"

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
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.metro.gradle.plugin)

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatformApplicationConventions") {
            id = "io.github.technoir42.conventions.kotlin-multiplatform-application"
            implementationClass = "io.github.technoir42.conventions.kotlin.multiplatform.KotlinMultiplatformApplicationConventionPlugin"
        }
        register("kotlinMultiplatformLibraryConventions") {
            id = "io.github.technoir42.conventions.kotlin-multiplatform-library"
            implementationClass = "io.github.technoir42.conventions.kotlin.multiplatform.KotlinMultiplatformLibraryConventionPlugin"
        }
    }
}
