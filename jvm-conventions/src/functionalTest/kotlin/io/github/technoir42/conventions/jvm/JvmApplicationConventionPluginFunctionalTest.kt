package io.github.technoir42.conventions.jvm

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class JvmApplicationConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("jvm-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":jvm-application:build")
    }

    @Test
    fun running() {
        val buildResult = gradleRunner.build(":jvm-application:run")

        assertThat(buildResult.output).contains("Hello, world!")
    }
}
