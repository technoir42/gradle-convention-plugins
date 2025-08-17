package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

fun Project.configureKotlin() {
    configure<KotlinJvmProjectExtension> {
        configureCompilerOptions()
    }

    dependencies {
        implementation(platform(CommonDependencies.KOTLIN_BOM))
        implementation(platform(CommonDependencies.KOTLINX_COROUTINES_BOM))
        implementation(platform(CommonDependencies.KOTLINX_SERIALIZATION_BOM))
    }
}

fun HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>.configureCompilerOptions() {
    compilerOptions {
        jvmDefault.set(JvmDefaultMode.NO_COMPATIBILITY)
        optIn.add("kotlin.time.ExperimentalTime")
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xconsistent-data-class-copy-visibility",
            "-Xnested-type-aliases",
        )
    }
}
