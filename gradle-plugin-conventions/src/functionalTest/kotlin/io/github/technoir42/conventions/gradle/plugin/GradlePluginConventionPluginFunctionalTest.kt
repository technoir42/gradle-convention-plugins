package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class GradlePluginConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("gradle-plugin-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":gradle-plugin:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.projectDir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":gradle-plugin:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "gradle-plugin", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**gradle-plugin-v1*")
    }
}
