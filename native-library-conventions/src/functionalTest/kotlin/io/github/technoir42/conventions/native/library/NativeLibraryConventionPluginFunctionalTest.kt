package io.github.technoir42.conventions.native.library

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
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
}
