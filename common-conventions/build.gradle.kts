import com.github.gmazzo.buildconfig.BuildConfigExtension

plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    packageName = "io.github.technoir42.conventions.common"

    buildFeatures {
        buildConfig = true
    }
}

afterEvaluate {
    configure<BuildConfigExtension> {
        forClass("DependencyVersions") {
            buildConfigField<String>("JUNIT", libs.versions.junit)
            buildConfigField<String>("KOTLINX_COROUTINES", libs.versions.kotlinx.coroutines)
            buildConfigField<String>("KOTLINX_SERIALIZATION", libs.versions.kotlinx.serialization)
        }
    }
}

dependencies {
    implementation(project(":gradle-extensions"))
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.serialization.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.buildconfig.gradle.plugin)

    testFixturesImplementation(gradleTestKit())
    testFixturesImplementation(libs.junit.jupiter.api)
}
