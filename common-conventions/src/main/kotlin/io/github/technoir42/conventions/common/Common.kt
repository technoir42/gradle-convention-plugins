package io.github.technoir42.conventions.common

import io.github.technoir42.conventions.common.api.ProjectSettings
import org.gradle.api.Project

fun Project.configureCommon(projectSettings: ProjectSettings) {
    group = projectSettings.groupId.get()
    version = projectSettings.version.get()
}
