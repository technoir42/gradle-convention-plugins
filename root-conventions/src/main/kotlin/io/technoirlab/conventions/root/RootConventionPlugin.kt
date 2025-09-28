package io.technoirlab.conventions.root

import io.technoirlab.conventions.root.configuration.configureDokka
import org.gradle.api.Plugin
import org.gradle.api.Project

class RootConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.gradle.lifecycle-base")

        configureDokka()
    }
}
