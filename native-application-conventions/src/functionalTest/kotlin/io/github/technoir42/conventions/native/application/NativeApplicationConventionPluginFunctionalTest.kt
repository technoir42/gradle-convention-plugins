package io.github.technoir42.conventions.native.application

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
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

        gradleRunner.build(":native-application:build")
    }
}
