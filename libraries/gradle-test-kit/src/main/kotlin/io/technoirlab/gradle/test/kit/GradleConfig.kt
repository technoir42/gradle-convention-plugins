package io.technoirlab.gradle.test.kit

@Suppress("LongParameterList")
class GradleConfig(
    var buildCache: Boolean = true,
    var configurationCache: Boolean = true,
    var configurationOnDemand: Boolean = true,
    var dryRun: Boolean = false,
    var isolatedProjects: Boolean = true,
    var warningsAsErrors: Boolean = false,
    val arguments: MutableList<String> = mutableListOf("--stacktrace"),
    val gradleProperties: MutableMap<String, Any> = mutableMapOf(),
    val systemProperties: MutableMap<String, Any> = mutableMapOf(),
    val environmentVariables: MutableMap<String, Any> = mutableMapOf(),
) {
    constructor(copy: GradleConfig) : this(
        buildCache = copy.buildCache,
        configurationCache = copy.configurationCache,
        configurationOnDemand = copy.configurationOnDemand,
        dryRun = copy.dryRun,
        isolatedProjects = copy.isolatedProjects,
        warningsAsErrors = copy.warningsAsErrors,
        arguments = copy.arguments.toMutableList(),
        gradleProperties = copy.gradleProperties.toMutableMap(),
        systemProperties = copy.systemProperties.toMutableMap(),
        environmentVariables = copy.environmentVariables.toMutableMap(),
    )
}
