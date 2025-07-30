package io.github.technoir42.conventions.native.application

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class NativeApplicationConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("native-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":native-application:build")
    }
}
