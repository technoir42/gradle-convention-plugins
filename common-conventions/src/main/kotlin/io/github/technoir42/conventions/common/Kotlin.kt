package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

fun Project.configureKotlin(kotlinVersion: KotlinVersion = KotlinVersion.DEFAULT) {
    configure<KotlinJvmProjectExtension> {
        compilerOptions {
            apiVersion.set(kotlinVersion)
            languageVersion.set(kotlinVersion)
        }
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}
