package io.github.technoir42.conventions.native.library

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class NativeLibraryConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("native-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":native-library:build")
    }

    @Test
    fun `default targets`() {
        val buildResult = gradleRunner.build(":native-library:tasks")

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
        gradleRunner.projectDir.resolve("native-library", "build.gradle.kts").appendText(
            // language=kotlin
            """
            nativeLibrary {
                defaultTargets = false
            }
            kotlin {
                linuxArm64()
            }
            """.trimIndent()
        )

        val buildResult = gradleRunner.build(":native-library:tasks")

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
        val projectDir = gradleRunner.projectDir.resolve("native-library")
        projectDir.resolve("build.gradle.kts").appendText(
            // language=kotlin
            """
            nativeLibrary {
                packageName = "com.example.native.library"
            }
            """.trimIndent()
        )

        projectDir.resolve("src", "nativeMain", "kotlin", "native", "library", "NativeLibrary.kt")
            .replaceText(
                """
                    import kotlinx.cinterop.ExperimentalForeignApi
                """.trimIndent(),
                """
                    import com.example.native.library.nativeGreet
                    import kotlinx.cinterop.ExperimentalForeignApi
                """.trimIndent()
            )

        gradleRunner.build(":native-library:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.projectDir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":native-library:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "native-library", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**native-library-v1*")
    }
}
