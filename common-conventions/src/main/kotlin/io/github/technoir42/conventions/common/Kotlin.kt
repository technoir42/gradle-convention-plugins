package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

fun Project.configureKotlin(kotlinVersion: KotlinVersion = KotlinVersion.DEFAULT, enableSerialization: Provider<Boolean>) {
    configure<KotlinJvmProjectExtension> {
        compilerOptions {
            apiVersion.set(kotlinVersion)
            languageVersion.set(kotlinVersion)
        }
    }

    afterEvaluate {
        if (enableSerialization.getOrElse(false)) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    }
}

private fun DependencyHandlerScope.implementation(dependencyNotation: Any): Dependency? =
    "implementation"(dependencyNotation)
