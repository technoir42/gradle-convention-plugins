package io.github.technoir42.conventions.jvm.library

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
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
                "publishRepositoryUrl" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "jvm-library", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**jvm-library-v1*")
    }
}
