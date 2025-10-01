package io.technoirlab.conventions.root

import io.technoirlab.conventions.common.ProjectSettingsImpl
import io.technoirlab.conventions.common.configuration.configureCommon
import io.technoirlab.conventions.root.configuration.configureDokka
import org.gradle.api.Plugin
import org.gradle.api.Project

class RootConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        check(path == ":") { "Root convention plugin must be applied to the root project." }

        pluginManager.apply("org.gradle.lifecycle-base")

        val projectSettings = ProjectSettingsImpl(this)
        configureCommon(projectSettings)
        configureDokka()
    }
}
