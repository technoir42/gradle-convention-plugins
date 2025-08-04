package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class KotlinMultiplatformLibraryConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("kotlin-multiplatform-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":kmp-library:build")
    }

    @Test
    fun `default targets`() {
        val buildResult = gradleRunner.build(":kmp-library:tasks")

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
        gradleRunner.projectDir.resolve("kmp-library", "build.gradle.kts").appendText(
            // language=kotlin
            """
            kotlinMultiplatformLibrary {
                defaultTargets = false
            }
            kotlin {
                linuxArm64()
            }
            """.trimIndent()
        )

        val buildResult = gradleRunner.build(":kmp-library:tasks")

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
        val projectDir = gradleRunner.projectDir.resolve("kmp-library")
        projectDir.resolve("build.gradle.kts").appendText(
            // language=kotlin
            """
            kotlinMultiplatformLibrary {
                packageName = "com.example.kmp.library"
            }
            """.trimIndent()
        )

        projectDir.resolve("src", "commonMain", "kotlin", "kmp", "library", "KmpLibrary.kt")
            .replaceText(
                """
                    import kotlinx.cinterop.ExperimentalForeignApi
                """.trimIndent(),
                """
                    import com.example.kmp.library.nativeGreet
                    import kotlinx.cinterop.ExperimentalForeignApi
                """.trimIndent()
            )

        gradleRunner.build(":kmp-library:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.projectDir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":kmp-library:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "kmp-library", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**kmp-library-v1*")
    }
}
