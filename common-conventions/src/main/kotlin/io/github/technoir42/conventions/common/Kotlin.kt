package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

fun Project.configureKotlin(kotlinVersion: KotlinVersion = KotlinVersion.DEFAULT) {
    configure<KotlinJvmProjectExtension> {
        compilerOptions {
            apiVersion.set(kotlinVersion)
            languageVersion.set(kotlinVersion)
        }
    }

    dependencies {
        implementation(platform(CommonDependencies.KOTLIN_BOM))
        implementation(platform(CommonDependencies.KOTLINX_COROUTINES_BOM))
        implementation(platform(CommonDependencies.KOTLINX_SERIALIZATION_BOM))
    }
}
