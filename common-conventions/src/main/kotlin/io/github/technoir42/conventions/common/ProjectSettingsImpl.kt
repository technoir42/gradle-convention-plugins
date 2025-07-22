package io.github.technoir42.conventions.common

import io.github.technoir42.conventions.common.api.ProjectSettings
import io.github.technoir42.conventions.common.api.ProjectSettings.Companion.DEFAULT_GROUP_ID
import io.github.technoir42.conventions.common.api.ProjectSettings.Companion.DEFAULT_VERSION
import io.github.technoir42.gradle.localGradleProperty
import org.gradle.api.Project
import org.gradle.api.provider.Provider

class ProjectSettingsImpl(private val project: Project) : ProjectSettings {
    override val groupId: Provider<String>
        get() = project.localGradleProperty("project.groupId").orElse(DEFAULT_GROUP_ID)

    override val version: Provider<String>
        get() = project.localGradleProperty("project.version").orElse(DEFAULT_VERSION)
}
