package io.github.technoir42.conventions.gradle.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.tasks.ValidatePlugins

internal fun Project.configurePlugin() {
    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}
