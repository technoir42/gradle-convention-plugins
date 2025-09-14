package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.configureBuildScript
import io.github.technoir42.conventions.common.fixtures.createDependencyGraph
import io.github.technoir42.conventions.common.fixtures.replaceText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.io.path.createParentDirectories
import kotlin.io.path.div
import kotlin.io.path.moveTo

class KotlinMultiplatformApplicationConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("kotlin-multiplatform-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":kmp-application:build")
    }

    @Test
    fun `dependency injection`() {
        gradleRunner.root.project("kmp-application")
            .configureBuildScript(
                """
                    kotlinMultiplatformApplication {
                        buildFeatures {
                            metro = true
                        }
                    }
                """.trimIndent()
            )
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
        gradleRunner.root.project("kmp-application")
            .configureBuildScript(
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
        val project = gradleRunner.root.project("kmp-application")
            .configureBuildScript(
                """
                    kotlinMultiplatformApplication {
                        packageName = "com.example.kmp.application"
                    }
                """.trimIndent()
            )

        val newMainKt = project.dir / "src/commonMain/kotlin/com/example/kmp/application/Main.kt"
        newMainKt.createParentDirectories()
        (project.dir / "src/commonMain/kotlin/kmp/application/Main.kt").moveTo(newMainKt)
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
        gradleRunner.root.project("kmp-application")
            .configureBuildScript(
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
