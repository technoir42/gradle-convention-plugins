package io.github.technoir42.conventions.jvm

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class JvmLibraryConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("jvm-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":jvm-library:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.projectDir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":jvm-library:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "jvm-library", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**jvm-library-v1*")
    }

    @Test
    fun `declaring common dependencies without versions`() {
        gradleRunner.projectDir.resolve("jvm-library", "build.gradle.kts").appendText(
            // language=kotlin
            """
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
            }
            """.trimIndent()
        )

        gradleRunner.build(":jvm-library:assemble")
    }

    @Test
    fun `ABI validation`() {
        val moduleDir = gradleRunner.projectDir.resolve("jvm-library")
        moduleDir.resolve("build.gradle.kts").appendText(
            """
                jvmLibrary {
                    buildFeatures {
                        abiValidation = true
                    }
                }
            """.trimIndent()
        )

        gradleRunner.build(":jvm-library:updateLegacyAbi")

        val abiDump = moduleDir.resolve("api", "jvm-library.api")
        assertThat(abiDump)
            .content()
            .contains(
                """
                    public final class com/example/jvm/library/JvmLibrary {
                    	public fun <init> ()V
                    }
                """.trimIndent()
            )

        moduleDir.resolve("src", "main", "kotlin", "com", "example", "jvm", "library", "JvmLibrary.kt")
            .writeText(
                """
                    package com.example.jvm.library
                    
                    class JvmLibrary {
                        fun hello() = Unit
                    }
                    
                """.trimIndent()
            )

        val buildResult = gradleRunner.buildAndFail(":jvm-library:check")

        assertThat(buildResult.task(":jvm-library:checkLegacyAbi")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(buildResult.output).contains(
            """
                |   public final class com/example/jvm/library/JvmLibrary {
                |   	public fun <init> ()V
                |  +	public final fun hello ()V
                |   }
            """.trimMargin()
        )
    }
}
