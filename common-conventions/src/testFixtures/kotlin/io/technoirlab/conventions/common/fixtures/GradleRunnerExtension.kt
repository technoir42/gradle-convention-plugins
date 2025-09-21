package io.technoirlab.conventions.common.fixtures

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.nio.file.Files
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

class GradleRunnerExtension(
    private val resourceDir: String,
    configuration: GradleConfig.() -> Unit = {}
) : BeforeEachCallback, AfterEachCallback {

    private val config = GradleConfig()
    lateinit var root: GradleProject

    init {
        config.configuration()
    }

    override fun beforeEach(context: ExtensionContext) {
        val projectDir = Files.createTempDirectory("project-")
        copyResources(resourceDir, projectDir)
        root = GradleProject(projectDir)
    }

    @OptIn(ExperimentalPathApi::class)
    override fun afterEach(context: ExtensionContext) {
        root.dir.deleteRecursively()
    }

    fun build(vararg tasks: String, configuration: GradleConfig.() -> Unit = {}): BuildResult {
        val config = GradleConfig(config)
        config.configuration()
        return createRunner(tasks, config).build()
    }

    fun buildAndFail(vararg tasks: String, configuration: GradleConfig.() -> Unit = {}): BuildResult {
        val config = GradleConfig(config)
        config.configuration()
        return createRunner(tasks, config).buildAndFail()
    }

    private fun createRunner(tasks: Array<out String>, config: GradleConfig): GradleRunner {
        val arguments = mutableListOf<String>()
        arguments += if (config.buildCache) "--build-cache" else "--no-build-cache"
        arguments += if (config.configurationCache) "--configuration-cache" else "--no-configuration-cache"
        arguments += if (config.configurationOnDemand) "--configure-on-demand" else "--no-configure-on-demand"
        arguments += if (config.dryRun) listOf("--dry-run") else emptyList()
        arguments += config.arguments
        arguments += config.gradleProperties.map { "-P${it.key}=${it.value}" }
        arguments += config.systemProperties.map { "-D${it.key}=${it.value}" }
        arguments += tasks

        @Suppress("SpreadOperator")
        return GradleRunner.create()
            .withArguments(*arguments.toTypedArray())
            .withProjectDir(root.dir.toFile())
            .withPluginClasspath()
            .apply {
                if (config.environmentVariables.isNotEmpty()) {
                    withEnvironment(config.environmentVariables.mapValues { "${it.value}" })
                }
            }
            .forwardOutput()
    }
}
