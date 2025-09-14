package io.github.technoir42.conventions.jvm

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.configureBuildScript
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

    @Test
    fun `declaring common dependencies without versions`() {
        gradleRunner.root.project("jvm-application").configureBuildScript(
            """
                dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-reflect")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
                }
            """.trimIndent()
        )

        gradleRunner.build(":jvm-application:assemble")
    }
}
