package io.github.technoir42.conventions.native.application

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class NativeApplicationConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("native-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":native-application:build")
    }

    @Test
    fun `default targets`() {
        val buildResult = gradleRunner.build(":native-application:tasks")

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
        gradleRunner.projectDir.resolve("native-application", "build.gradle.kts").appendText(
            // language=kotlin
            """
            nativeApplication {
                defaultTargets = false
            }
            kotlin {
                linuxArm64()
            }
            """.trimIndent()
        )

        val buildResult = gradleRunner.build(":native-application:tasks")

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
        val projectDir = gradleRunner.projectDir.resolve("native-application")
        projectDir.resolve("build.gradle.kts").appendText(
            // language=kotlin
            """
            nativeApplication {
                packageName = "com.example.native.application"
            }
            """.trimIndent()
        )

        val newMainKt = projectDir.resolve("src", "nativeMain", "kotlin", "com", "example", "native", "application", "Main.kt")
            .apply { parentFile.mkdirs() }
        projectDir.resolve("src", "nativeMain", "kotlin", "native", "application", "Main.kt").renameTo(newMainKt)
        newMainKt.replaceText("package native.application", "package com.example.native.application")

        val buildResult = gradleRunner.build(":native-application:runDebugExecutable")

        assertThat(buildResult.output).contains("Hello, world!")
    }

    @Test
    fun running() {
        val buildResult = gradleRunner.build(":native-application:runDebugExecutable")

        assertThat(buildResult.output).contains("Hello, world!")
    }
}
