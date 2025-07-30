package io.github.technoir42.conventions.common.fixtures

@Suppress("LongParameterList")
class GradleConfig(
    var buildCache: Boolean = true,
    var configurationCache: Boolean = true,
    var configurationOnDemand: Boolean = true,
    var dryRun: Boolean = false,
    var isolatedProjects: Boolean = true,
    val arguments: MutableList<String> = mutableListOf(),
    val gradleProperties: MutableMap<String, Any> = mutableMapOf(),
    val systemProperties: MutableMap<String, Any> = mutableMapOf(),
) {
    constructor(copy: GradleConfig) : this(
        buildCache = copy.buildCache,
        configurationCache = copy.configurationCache,
        configurationOnDemand = copy.configurationOnDemand,
        dryRun = copy.dryRun,
        isolatedProjects = copy.isolatedProjects,
        arguments = copy.arguments.toMutableList(),
        gradleProperties = copy.gradleProperties.toMutableMap(),
        systemProperties = copy.systemProperties.toMutableMap(),
    )
}
