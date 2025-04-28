package io.github.technoir42.conventions.settings

import io.github.technoir42.conventions.common.Environment
import io.github.technoir42.conventions.settings.api.SettingsExtension
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.create

class SettingsConventionPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) = with(settings) {
        val config = extensions.create<SettingsExtension>(SettingsExtension.NAME)

        gradle.settingsEvaluated {
            rootProject.name = config.projectId.get()
        }

        gradle.rootProject {
            pluginManager.apply("org.gradle.lifecycle-base")
        }

        gradle.afterProject {
            configureDependencyResolution()
        }

        val environment = Environment(providers)
        configureDependencyResolution(environment)
    }
}
