package io.github.technoir42.gradle.dependencies

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun <T : ModuleDependency> DependencyHandlerScope.api(dependency: T, dependencyConfiguration: T.() -> Unit): T =
    "api".invoke(dependency, dependencyConfiguration)

fun DependencyHandlerScope.implementation(dependencyNotation: Any): Dependency? =
    "implementation"(dependencyNotation)
