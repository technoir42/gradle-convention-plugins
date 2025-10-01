package io.technoirlab.conventions.common

import io.technoirlab.conventions.common.api.CommonExtension
import io.technoirlab.conventions.common.configuration.configureDependencySorting
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Common conventions for all projects.
 *
 * DSL: [CommonExtension]
 */
class CommonConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        configureDependencySorting()
    }
}
