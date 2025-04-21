package io.github.technoir42.conventions.common

import org.gradle.api.Project

fun Project.configureCommon() {
    group = providers.gradleProperty("project.groupId").get()
    version = providers.gradleProperty("project.version").get()
}
