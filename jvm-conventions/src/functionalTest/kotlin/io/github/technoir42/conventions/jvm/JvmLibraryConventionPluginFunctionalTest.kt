package io.github.technoir42.conventions.jvm

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.configureBuildScript
import io.github.technoir42.conventions.common.fixtures.jarEntries
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.writeText

class JvmLibraryConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("jvm-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":jvm-library:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.root.dir / "repo"
        repoDir.createDirectories()

        gradleRunner.build(":jvm-library:publish") {
            gradleProperties += mapOf("publish.url" to repoDir.toUri())
        }

        val artifactDir = repoDir / "io/github/technoir42/jvm-library/dev"
        assertThat(artifactDir)
            .isDirectoryContaining("glob:**jvm-library-dev.*")
            .isDirectoryContaining("glob:**jvm-library-dev-sources.*")

        val sourcesJar = artifactDir / "jvm-library-dev-sources.jar"
        assertThat(sourcesJar).exists()
        assertThat(sourcesJar.jarEntries()).contains("com/example/jvm/library/JvmLibrary.kt")
    }

    @Test
    fun `publishing with custom Maven coordinates`() {
        val repoDir = gradleRunner.root.dir / "repo"
        repoDir.createDirectories()

        gradleRunner.build(":jvm-library:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toUri()
            )
        }

        val artifactDir = repoDir / "com/example/jvm-library/v1"
        assertThat(artifactDir).isDirectoryContaining("glob:**jvm-library-v1*")
    }

    @Test
    fun `declaring common dependencies without versions`() {
        gradleRunner.root.project("jvm-library")
            .configureBuildScript(
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
        val project = gradleRunner.root.project("jvm-library")
            .configureBuildScript(
                """
                    jvmLibrary {
                        buildFeatures {
                            abiValidation = true
                        }
                    }
                """.trimIndent()
            )

        gradleRunner.build(":jvm-library:updateLegacyAbi")

        val abiDump = project.dir / "api/jvm-library.api"
        assertThat(abiDump)
            .content()
            .contains(
                """
                    public final class com/example/jvm/library/JvmLibrary {
                    	public fun <init> ()V
                    }
                """.trimIndent()
            )

        (project.dir / "src/main/kotlin/com/example/jvm/library/JvmLibrary.kt")
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
