package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

fun Project.configureKotlin(enableAbiValidation: Provider<Boolean>) {
    extensions.configure(KotlinJvmProjectExtension::class) {
        configureCompilerOptions()

        @OptIn(ExperimentalAbiValidation::class)
        extensions.configure(AbiValidationExtension::class) {
            enabled.set(enableAbiValidation)
        }
    }

    dependencies {
        implementation(platform(CommonDependencies.KOTLIN_BOM))
        implementation(platform(CommonDependencies.KOTLINX_COROUTINES_BOM))
        implementation(platform(CommonDependencies.KOTLINX_SERIALIZATION_BOM))
    }

    tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).configure {
        if (enableAbiValidation.get()) {
            dependsOn(tasks.named("checkLegacyAbi"))
        }
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
