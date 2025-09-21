package io.technoirlab.conventions.jvm

import io.technoirlab.gradle.test.kit.Generator
import io.technoirlab.gradle.test.kit.GradleRunnerExtension
import io.technoirlab.gradle.test.kit.configureBuildScript
import io.technoirlab.gradle.test.kit.generatedFile
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
    fun `BuildConfig generation`() {
        val project = gradleRunner.root.project("jvm-application")
            .configureBuildScript(
                """
                    jvmApplication {
                        buildFeatures {
                            buildConfig {
                                buildConfigField<String>("STRING_FIELD", "string value")
                                buildConfigField<String>("LAZY_STRING_FIELD", provider { project.description })
                                buildConfigField<String>("NONEXISTENT_STRING_FIELD", provider { null })
                                buildConfigField<String>("NULLABLE_STRING_FIELD", null)
                                buildConfigField<Boolean>("BOOLEAN_FIELD", true)
                                buildConfigField<Int>("INT_FIELD", 42)
                                buildConfigField<String>("TEST_STRING_FIELD", "test string value", variant = "test")
                            }
                        }
                    }
                    
                    description = "Project description"
                """.trimIndent()
            )

        gradleRunner.build(":jvm-application:classes")

        assertThat(project.generatedFile(Generator.BuildConfig, "com.example.jvm.application.BuildConfig"))
            .content()
            .contains("const val STRING_FIELD: String = \"string value\"")
            .contains("const val LAZY_STRING_FIELD: String = \"Project description\"")
            .contains("val NULLABLE_STRING_FIELD: String? = null")
            .contains("const val BOOLEAN_FIELD: Boolean = true")
            .contains("const val INT_FIELD: Int = 42")
            .doesNotContain("NONEXISTENT_STRING_FIELD")
            .doesNotContain("TEST_STRING_FIELD")

        gradleRunner.build(":jvm-application:testClasses")

        assertThat(project.generatedFile(Generator.BuildConfig, "com.example.jvm.application.TestBuildConfig", variant = "test"))
            .content()
            .contains("const val TEST_STRING_FIELD: String = \"test string value\"")
            .doesNotContain("INT_FIELD")
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
