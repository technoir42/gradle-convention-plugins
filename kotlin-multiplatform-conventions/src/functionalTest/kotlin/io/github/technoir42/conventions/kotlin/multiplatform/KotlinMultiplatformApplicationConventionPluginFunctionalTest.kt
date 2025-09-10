package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.createDependencyGraph
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class KotlinMultiplatformApplicationConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("kotlin-multiplatform-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":kmp-application:build")
    }

    @Test
    fun `dependency injection`() {
        gradleRunner.projectDir.resolve("kmp-application", "build.gradle.kts").appendText(
            //language=kotlin
            """
                kotlinMultiplatformApplication {
                    buildFeatures {
                        metro = true
                    }
                }
            """.trimIndent()
        )
        gradleRunner.projectDir.resolve("kmp-application")
            .createDependencyGraph()

        gradleRunner.build(":kmp-application:assemble")
    }

    @Test
    fun `default targets`() {
        val buildResult = gradleRunner.build(":kmp-application:tasks")

        assertThat(buildResult.output).contains(
            "androidNativeArm64",
            "iosArm64",
            "iosSimulatorArm64",
            "linuxX64",
            "macosArm64",
            "mingwX64",
        )
    }

    @Test
    fun `custom targets`() {
        gradleRunner.projectDir.resolve("kmp-application", "build.gradle.kts").appendText(
            // language=kotlin
            """
            kotlinMultiplatformApplication {
                defaultTargets = false
            }
            kotlin {
                linuxArm64()
            }
            """.trimIndent()
        )

        val buildResult = gradleRunner.build(":kmp-application:tasks")

        assertThat(buildResult.output)
            .contains(
                "linuxArm64",
            )
            .doesNotContain(
                "androidNativeArm64",
                "iosArm64",
                "iosSimulatorArm64",
                "linuxX64",
                "macosArm64",
                "mingwX64",
            )
    }

    @Test
    fun `custom package name`() {
        val projectDir = gradleRunner.projectDir.resolve("kmp-application")
        projectDir.resolve("build.gradle.kts").appendText(
            // language=kotlin
            """
            kotlinMultiplatformApplication {
                packageName = "com.example.kmp.application"
            }
            """.trimIndent()
        )

        val newMainKt = projectDir.resolve("src", "commonMain", "kotlin", "com", "example", "kmp", "application", "Main.kt")
            .apply { parentFile.mkdirs() }
        projectDir.resolve("src", "commonMain", "kotlin", "kmp", "application", "Main.kt").renameTo(newMainKt)
        newMainKt.replaceText("package kmp.application", "package com.example.kmp.application")

        val buildResult = gradleRunner.build(":kmp-application:runDebugExecutable")

        assertThat(buildResult.output).contains("Hello, world!")
    }

    @Test
    fun running() {
        val buildResult = gradleRunner.build(":kmp-application:runDebugExecutable")

        assertThat(buildResult.output).contains("Hello, world!")
    }

    @Test
    fun `declaring common dependencies without versions`() {
        gradleRunner.projectDir.resolve("kmp-application", "build.gradle.kts").appendText(
            // language=kotlin
            """
            kotlin {
                sourceSets {
                    commonMain.dependencies {
                        implementation("org.jetbrains.kotlin:kotlin-reflect")
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
                    }
                }
            }
            """.trimIndent()
        )

        gradleRunner.build(":kmp-application:assemble")
    }
}
