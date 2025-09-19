plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.github.technoir42.conventions.common"

    buildFeatures {
        abiValidation = true
        buildConfig {
            buildConfigField<String>("JUNIT5_VERSION", libs.versions.junit5)
            buildConfigField<String>("KOTLIN_BOM", libs.kotlin.bom.map { it.toString() })
            buildConfigField<String>("KOTLINX_COROUTINES_BOM", libs.kotlinx.coroutines.bom.map { it.toString() })
            buildConfigField<String>("KOTLINX_SERIALIZATION_BOM", libs.kotlinx.serialization.bom.map { it.toString() })
        }
    }
}

dependencies {
    implementation(project(":gradle-extensions"))
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.buildconfig.gradle.plugin)

    runtimeOnly(libs.kotlin.serialization.gradle.plugin)
    runtimeOnly(libs.nmcp.gradle.plugin)

    testFixturesImplementation(gradleTestKit())
    testFixturesImplementation(libs.junit.jupiter.api)
}
