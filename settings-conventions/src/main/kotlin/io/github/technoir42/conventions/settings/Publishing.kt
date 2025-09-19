package io.github.technoir42.conventions.settings

import io.github.technoir42.conventions.settings.NmcpProjectCollectorService.Companion.getProjectCollectorService
import nmcp.NmcpAggregationExtension
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Settings.configurePublishing() {
    gradle.lifecycle.afterProject {
        if (path == ":") {
            configureRootProject()
        } else {
            configureSubproject()
        }
    }

    gradle.projectsEvaluated {
        gradle.rootProject {
            val projectsToPublish = getProjectCollectorService().get().projectPaths
            dependencies {
                projectsToPublish.forEach {
                    "nmcpAggregation"(project(it))
                }
            }
        }
    }
}

private fun Project.configureRootProject() {
    pluginManager.apply("com.gradleup.nmcp.aggregation")

    extensions.configure(NmcpAggregationExtension::class) {
        centralPortal {
            username.set(providers.environmentVariable("CENTRAL_PORTAL_USER"))
            password.set(providers.environmentVariable("CENTRAL_PORTAL_PASSWORD"))
            publishingType.set("USER_MANAGED")
        }
    }
}

private fun Project.configureSubproject() {
    if (pluginManager.hasPlugin("com.gradleup.nmcp")) {
        getProjectCollectorService().get().projectPaths += path
    }
}
