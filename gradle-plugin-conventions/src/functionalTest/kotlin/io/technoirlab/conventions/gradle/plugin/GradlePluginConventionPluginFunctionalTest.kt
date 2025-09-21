package io.technoirlab.conventions.gradle.plugin

import io.technoirlab.conventions.common.fixtures.POM_EXPECTED
import io.technoirlab.conventions.common.fixtures.PROJECT_METADATA
import io.technoirlab.gradle.test.kit.Generator
import io.technoirlab.gradle.test.kit.GradleRunnerExtension
import io.technoirlab.gradle.test.kit.buildDir
import io.technoirlab.gradle.test.kit.configureBuildScript
import io.technoirlab.gradle.test.kit.generatedFile
import io.technoirlab.gradle.test.kit.jarEntries
import io.technoirlab.gradle.test.kit.replaceText
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class GradlePluginConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("gradle-plugin-project")

    @Test
    fun `builds successfully`() {
        gradleRunner.build(":example-plugin:build")
    }

    @Test
    fun `BuildConfig generation`() {
        val project = gradleRunner.root.project("example-plugin")
            .configureBuildScript(
                """
                    gradlePluginConfig {
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

        gradleRunner.build(":example-plugin:classes")

        assertThat(project.generatedFile(Generator.BuildConfig, "com.example.plugin.BuildConfig"))
            .content()
            .contains("const val STRING_FIELD: String = \"string value\"")
            .contains("const val LAZY_STRING_FIELD: String = \"Project description\"")
            .contains("val NULLABLE_STRING_FIELD: String? = null")
            .contains("const val BOOLEAN_FIELD: Boolean = true")
            .contains("const val INT_FIELD: Int = 42")
            .doesNotContain("NONEXISTENT_STRING_FIELD")
            .doesNotContain("TEST_STRING_FIELD")

        gradleRunner.build(":example-plugin:testClasses")

        assertThat(project.generatedFile(Generator.BuildConfig, "com.example.plugin.TestBuildConfig", variant = "test"))
            .content()
            .contains("const val TEST_STRING_FIELD: String = \"test string value\"")
            .doesNotContain("INT_FIELD")
    }

    @Test
    fun publishing() {
        val repoDir = gradleRunner.root.dir / "repo"
        repoDir.createDirectories()

        gradleRunner.build(":example-plugin:publish") {
            gradleProperties += mapOf("publish.url" to repoDir.toUri())
        }

        val artifactDir = repoDir / "io/technoirlab/example-plugin/dev"
        assertThat(artifactDir)
            .isDirectoryContaining("glob:**example-plugin-dev.*")
            .isDirectoryContaining("glob:**example-plugin-dev-sources.*")
            .isDirectoryContaining("glob:**example-plugin-dev-javadoc.*")
            .isDirectoryContaining("glob:**example-plugin-dev-api.*")
            .isDirectoryContaining("glob:**example-plugin-dev-api-sources.*")

        val pomFile = artifactDir / "example-plugin-dev.pom"
        assertThat(pomFile)
            .content()
            .contains("<name>example-plugin</name>")

        val sourcesJar = artifactDir / "example-plugin-dev-sources.jar"
        assertThat(sourcesJar).exists()
        assertThat(sourcesJar.jarEntries()).contains("com/example/plugin/ExamplePlugin.kt")

        val javadocJar = artifactDir / "example-plugin-dev-javadoc.jar"
        assertThat(javadocJar).exists()
        assertThat(javadocJar.jarEntries()).contains("com/example/plugin/ExamplePlugin.html")
        assertThat(javadocJar.jarEntries()).contains("com/example/plugin/api/ExampleExtension.html")

        val apiSourcesJar = artifactDir / "example-plugin-dev-api-sources.jar"
        assertThat(apiSourcesJar).exists()
        assertThat(apiSourcesJar.jarEntries()).contains("com/example/plugin/api/ExampleExtension.kt")
    }

    @Test
    fun `publishing with custom coordinates`() {
        val repoDir = gradleRunner.root.dir / "repo"
        repoDir.createDirectories()

        gradleRunner.build(":example-plugin:publish") {
            gradleProperties += mapOf(
                "project.groupId" to "com.example",
                "project.version" to "v1",
                "publish.url" to repoDir.toUri()
            )
        }

        val artifactDir = repoDir / "com/example/example-plugin/v1"
        assertThat(artifactDir).isDirectoryContaining("glob:**example-plugin-v1*")
    }

    @Test
    fun `publishing with custom metadata`() {
        val repoDir = gradleRunner.root.dir / "repo"
        repoDir.createDirectories()

        gradleRunner.root.project("example-plugin")
            .configureBuildScript("gradlePluginConfig {\n${PROJECT_METADATA.prependIndent("    ")}\n}")

        gradleRunner.build(":example-plugin:publish") {
            gradleProperties += mapOf("publish.url" to repoDir.toUri())
            environmentVariables += mapOf(
                "GITHUB_SERVER_URL" to "https://github.com",
                "GITHUB_REPOSITORY" to "example-org/example-project",
            )
        }

        val pomFile = repoDir / "io/technoirlab/example-plugin/dev/example-plugin-dev.pom"
        assertThat(pomFile)
            .content()
            .containsIgnoringNewLines(*POM_EXPECTED)
    }

    @Test
    fun `ABI validation`() {
        val project = gradleRunner.root.project("example-plugin")
            .configureBuildScript(
                """
                    gradlePluginConfig {
                        buildFeatures {
                            abiValidation = true
                        }
                    }
                """.trimIndent()
            )

        gradleRunner.build(":example-plugin:updateLegacyAbi")

        val abiDump = project.dir / "api/example-plugin.api"
        assertThat(abiDump)
            .content()
            .contains(
                """
                    public final class com/example/plugin/ExamplePlugin : org/gradle/api/Plugin {
                    	public fun <init> ()V
                    	public synthetic fun apply (Ljava/lang/Object;)V
                    	public fun apply (Lorg/gradle/api/Project;)V
                    }
                """.trimIndent()
            )

        (project.dir / "src/main/kotlin/com/example/plugin/ExamplePlugin.kt")
            .replaceText(
                """
                    |    override fun apply(project: Project) {
                    |        project.extensions.create<ExampleExtension>(ExampleExtension.NAME)
                    |    }
                """.trimMargin(),
                """
                    |    override fun apply(project: Project) {
                    |        project.extensions.create<ExampleExtension>(ExampleExtension.NAME)
                    |    }
                    |
                    |    fun hello() = Unit
                """.trimMargin()
            )

        val buildResult = gradleRunner.buildAndFail(":example-plugin:check")

        assertThat(buildResult.task(":example-plugin:checkLegacyAbi")?.outcome).isEqualTo(TaskOutcome.FAILED)
        assertThat(buildResult.output).contains(
            """
                |   	public fun apply (Lorg/gradle/api/Project;)V
                |  +	public final fun hello ()V
            """.trimMargin()
        )
    }

    @Test
    fun `generating documentation`() {
        gradleRunner.build(":example-plugin:dokkaGenerate")

        val project = gradleRunner.root.project("example-plugin")
        assertThat(project.buildDir / "dokka/html/index.html").exists()
        assertThat(project.buildDir / "dokka/html/example-plugin/com.example.plugin/index.html").exists()
        assertThat(project.buildDir / "dokka/html/example-plugin/com.example.plugin.api/index.html").exists()
    }
}
