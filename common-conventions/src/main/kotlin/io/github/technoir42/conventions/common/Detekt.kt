package io.github.technoir42.conventions.common

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.gradle.language.base.plugins.LifecycleBasePlugin

fun Project.configureDetekt() {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    configure<DetektExtension> {
        config.from(layout.settingsDirectory.dir("config").file("detekt.yaml"))
        buildUponDefaultConfig = true
    }

    val detektConfig = the<DetektExtension>()
    configurations.configureEach {
        resolutionStrategy.eachDependency {
            if (requested.group == DETEKT_GROUP_ID && requested.version.isNullOrEmpty()) {
                useVersion(detektConfig.toolVersion)
            }
        }
    }

    tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).configure {
        dependsOn(tasks.named("detektMain"))
        dependsOn(tasks.named("detektTest"))
    }

    dependencies {
        detektPlugins("$DETEKT_GROUP_ID:detekt-formatting")
    }
}

private fun DependencyHandlerScope.detektPlugins(dependencyNotation: Any) =
    "detektPlugins"(dependencyNotation)

private const val DETEKT_GROUP_ID = "io.gitlab.arturbosch.detekt"
