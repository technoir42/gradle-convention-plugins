package io.technoirlab.conventions.common

import com.squareup.sort.SortDependenciesExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class CommonConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("com.squareup.sort-dependencies")

        extensions.configure(SortDependenciesExtension::class) {
            check(false)
        }
    }
}
