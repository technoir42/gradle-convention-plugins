plugins {
    id("io.technoirlab.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.technoirlab.conventions.common"

    buildFeatures {
        abiValidation = true

        buildConfig {
            buildConfigField("GROUP_ID", provider { "$group" })
            buildConfigField("VERSION", provider { "$version" })
            buildConfigField("JUNIT5_VERSION", libs.versions.junit5)
            buildConfigField("KOTLIN_BOM", libs.kotlin.bom.map { it.toString() })
            buildConfigField("KOTLINX_COROUTINES_BOM", libs.kotlinx.coroutines.bom.map { it.toString() })
            buildConfigField("KOTLINX_SERIALIZATION_BOM", libs.kotlinx.serialization.bom.map { it.toString() })
        }
    }
}

dependencies {
    implementation(project(":libraries:gradle-extensions"))
    implementation(libs.buildconfig.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.sort.dependencies.gradle.plugin)

    functionalTestImplementation(project(":libraries:gradle-test-kit"))
    functionalTestImplementation(libs.assertj.core)

    runtimeOnly(libs.kotlin.serialization.gradle.plugin)
    runtimeOnly(libs.nmcp.gradle.plugin)

    testFixturesImplementation(project(":libraries:gradle-test-kit"))
}

gradlePlugin {
    plugins {
        register("commonConventions") {
            id = "io.technoirlab.conventions.common"
            implementationClass = "io.technoirlab.conventions.common.CommonConventionPlugin"
        }
    }
}
