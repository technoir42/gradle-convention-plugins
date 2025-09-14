package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.configureBuildScript
import io.github.technoir42.conventions.common.fixtures.jarEntries
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class GradlePluginConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("gradle-plugin-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":example-plugin:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.root.dir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":example-plugin:publish") {
            gradleProperties += mapOf("publish.url" to repoDir.toURI())
        }

        val artifactDir = repoDir.resolve("io", "github", "technoir42", "example-plugin", "dev")
        assertThat(artifactDir)
            .isDirectoryContaining("glob:**example-plugin-dev.*")
            .isDirectoryContaining("glob:**example-plugin-dev-sources.*")
            .isDirectoryContaining("glob:**example-plugin-dev-api.*")
            .isDirectoryContaining("glob:**example-plugin-dev-api-sources.*")

        val sourcesJar = artifactDir.resolve("example-plugin-dev-sources.jar")
        assertThat(sourcesJar).exists()
        assertThat(sourcesJar.jarEntries()).contains("com/example/plugin/ExamplePlugin.kt")

        val apiSourcesJar = artifactDir.resolve("example-plugin-dev-api-sources.jar")
        assertThat(apiSourcesJar).exists()
        assertThat(apiSourcesJar.jarEntries()).contains("com/example/plugin/api/ExampleExtension.kt")
    }

    @Test
    fun `publishing with custom coordinates`() {
        val repoDir = gradleRunner.root.dir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":example-plugin:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "example-plugin", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**example-plugin-v1*")
    }

    @Test
    fun `ABI validation`() {
        val project = gradleRunner.root.project("example-plugin")
            .configureBuildScript(
                """
                    gradlePluginConfig {
                        buildFeatures {
                            abiValidation = true
                        }
                    }
                """.trimIndent()
            )

        gradleRunner.build(":example-plugin:updateLegacyAbi")

        val abiDump = project.dir.resolve("api", "example-plugin.api")
        assertThat(abiDump)
            .content()
            .contains(
                """
                    public final class com/example/plugin/ExamplePlugin : org/gradle/api/Plugin {
                    	public fun <init> ()V
                    	public synthetic fun apply (Ljava/lang/Object;)V
                    	public fun apply (Lorg/gradle/api/Project;)V
                    }
                """.trimIndent()
            )

        project.dir.resolve("src", "main", "kotlin", "com", "example", "plugin", "ExamplePlugin.kt")
            .replaceText(
                """
                    |    override fun apply(project: Project) {
                    |        project.extensions.create<ExampleExtension>(ExampleExtension.NAME)
                    |    }
                """.trimMargin(),
                """
                    |    override fun apply(project: Project) {
                    |        project.extensions.create<ExampleExtension>(ExampleExtension.NAME)
                    |    }
                    |
                    |    fun hello() = Unit
                """.trimMargin()
            )

        val buildResult = gradleRunner.buildAndFail(":example-plugin:check")

        assertThat(buildResult.task(":example-plugin:checkLegacyAbi")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(buildResult.output).contains(
            """
                |   	public fun apply (Lorg/gradle/api/Project;)V
                |  +	public final fun hello ()V
            """.trimMargin()
        )
    }
}
