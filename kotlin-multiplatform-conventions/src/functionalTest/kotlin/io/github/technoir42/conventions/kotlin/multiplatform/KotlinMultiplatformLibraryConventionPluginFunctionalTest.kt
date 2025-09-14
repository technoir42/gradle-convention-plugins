package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.fixtures.GradleRunnerExtension
import io.github.technoir42.conventions.common.fixtures.configureBuildScript
import io.github.technoir42.conventions.common.fixtures.createDependencyGraph
import io.github.technoir42.conventions.common.fixtures.jarEntries
import io.github.technoir42.conventions.common.fixtures.replaceText
import io.github.technoir42.conventions.common.fixtures.resolve
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
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
    fun `dependency injection`() {
        gradleRunner.root.project("kmp-library")
            .configureBuildScript(
                """
                    kotlinMultiplatformLibrary {
                        buildFeatures {
                            metro = true
                        }
                    }
                """.trimIndent()
            )
            .createDependencyGraph()

        gradleRunner.build(":kmp-library:assemble")
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
        gradleRunner.root.project("kmp-library")
            .configureBuildScript(
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
        val project = gradleRunner.root.project("kmp-library")
            .configureBuildScript(
                """
                    kotlinMultiplatformLibrary {
                        packageName = "com.example.kmp.library"
                    }
                """.trimIndent()
            )

        project.dir.resolve("src", "commonMain", "kotlin", "kmp", "library", "KmpLibrary.kt")
            .replaceText(
                """
                    package kmp.library
                """.trimIndent(),
                """
                    package kmp.library
                    
                    import com.example.kmp.library.nativeGreet
                """.trimIndent()
            )

        gradleRunner.build(":kmp-library:build")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.root.dir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":kmp-library:publish") {
            gradleProperties += mapOf("publish.url" to repoDir.toURI())
        }

        val artifactDir = repoDir.resolve("io", "github", "technoir42", "kmp-library", "dev")
        assertThat(artifactDir)
            .isDirectoryContaining("glob:**kmp-library-dev.*")
            .isDirectoryContaining("glob:**kmp-library-dev-sources.*")

        val sourcesJar = artifactDir.resolve("kmp-library-dev-sources.jar")
        assertThat(sourcesJar).exists()
        assertThat(sourcesJar.jarEntries()).contains("commonMain/kmp/library/KmpLibrary.kt")
    }

    @Test
    fun `publishing with custom Maven coordinates`() {
        val repoDir = gradleRunner.root.dir.resolve("repo")
        repoDir.mkdirs()

        gradleRunner.build(":kmp-library:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example.kmp",
                "project.version" to "v1",
                "publish.url" to repoDir.toURI()
            )
        }

        val artifactDir = repoDir.resolve("com", "example", "kmp", "kmp-library", "v1")
        assertThat(artifactDir).isDirectoryContaining("glob:**kmp-library-v1*")
    }

    @Test
    fun `declaring common dependencies without versions`() {
        gradleRunner.root.project("kmp-library").configureBuildScript(
            """
                kotlin {
                    sourceSets {
                        commonMain.dependencies {
                            implementation("org.jetbrains.kotlin:kotlin-reflect")
                            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                            implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
                        }
                    }
                }
            """.trimIndent()
        )

        gradleRunner.build(":kmp-library:assemble")
    }

    @Test
    fun `ABI validation`() {
        val project = gradleRunner.root.project("kmp-library")
            .configureBuildScript(
                """
                    kotlinMultiplatformLibrary {
                        buildFeatures {
                            abiValidation = true
                        }
                    }
                """.trimIndent()
            )

        gradleRunner.build(":kmp-library:updateLegacyAbi")

        val abiDump = project.dir.resolve("api", "kmp-library.klib.api")
        assertThat(abiDump)
            .content()
            .contains(
                """
                    // Library unique name: <io.github.technoir42:kmp-library>
                    final fun kmp.library/greet(kotlin/String) // kmp.library/greet|greet(kotlin.String){}[0]
                """.trimIndent()
            )

        project.dir.resolve("src", "commonMain", "kotlin", "kmp", "library", "KmpLibrary.kt")
            .writeText(
                """
                    package kmp.library
                    
                    fun greet(name: String) {
                        nativeGreet(name)
                    }
                    
                    fun hello() = Unit
                    
                """.trimIndent()
            )

        val buildResult = gradleRunner.buildAndFail(":kmp-library:check")

        assertThat(buildResult.task(":kmp-library:checkLegacyAbi")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(buildResult.output).contains(
            """
                |  // Library unique name: <io.github.technoir42:kmp-library>
                |   final fun kmp.library/greet(kotlin/String) // kmp.library/greet|greet(kotlin.String){}[0]
                |  +final fun kmp.library/hello() // kmp.library/hello|hello(){}[0]
            """.trimMargin()
        )
    }
}
