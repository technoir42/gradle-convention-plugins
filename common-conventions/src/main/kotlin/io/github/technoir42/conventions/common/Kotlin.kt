package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

fun Project.configureKotlin() {
    configure<KotlinJvmProjectExtension> {
        compilerOptions {
            optIn.add("kotlin.time.ExperimentalTime")
            freeCompilerArgs.addAll(
                "-Xcontext-parameters",
                "-Xconsistent-data-class-copy-visibility"
            )
        }
    }

    dependencies {
        implementation(platform(CommonDependencies.KOTLIN_BOM))
        implementation(platform(CommonDependencies.KOTLINX_COROUTINES_BOM))
        implementation(platform(CommonDependencies.KOTLINX_SERIALIZATION_BOM))
    }
}
