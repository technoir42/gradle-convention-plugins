plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.technoirlab.conventions.kotlin.multiplatform"

    buildFeatures {
        abiValidation = true
        buildConfig {
            buildConfigField<String>("KOTLIN_BOM", libs.kotlin.bom.map { it.toString() })
            buildConfigField<String>("KOTLINX_COROUTINES_BOM", libs.kotlinx.coroutines.bom.map { it.toString() })
            buildConfigField<String>("KOTLINX_SERIALIZATION_BOM", libs.kotlinx.serialization.bom.map { it.toString() })
        }
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
    implementation(libs.kotlin.gradle.plugin)

    runtimeOnly(libs.metro.gradle.plugin)

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatformApplicationConventions") {
            id = "io.technoirlab.conventions.kotlin-multiplatform-application"
            implementationClass = "io.technoirlab.conventions.kotlin.multiplatform.KotlinMultiplatformApplicationConventionPlugin"
        }
        register("kotlinMultiplatformLibraryConventions") {
            id = "io.technoirlab.conventions.kotlin-multiplatform-library"
            implementationClass = "io.technoirlab.conventions.kotlin.multiplatform.KotlinMultiplatformLibraryConventionPlugin"
        }
    }
}
