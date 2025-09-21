package io.technoirlab.conventions.common.configuration

import io.technoirlab.conventions.common.api.ProjectSettings
import org.gradle.api.Project

fun Project.configureCommon(projectSettings: ProjectSettings) {
    group = projectSettings.groupId.get()
    version = projectSettings.version.get()
}
