package io.technoirlab.conventions.common

import io.technoirlab.conventions.common.api.ProjectSettings
import io.technoirlab.conventions.common.api.ProjectSettings.Companion.DEFAULT_GROUP_ID
import io.technoirlab.conventions.common.api.ProjectSettings.Companion.DEFAULT_VERSION
import io.technoirlab.gradle.localGradleProperty
import org.gradle.api.Project
import org.gradle.api.provider.Provider

class ProjectSettingsImpl(private val project: Project) : ProjectSettings {
    override val groupId: Provider<String>
        get() = project.localGradleProperty("project.groupId").orElse(DEFAULT_GROUP_ID)

    override val version: Provider<String>
        get() = project.localGradleProperty("project.version").orElse(DEFAULT_VERSION)
}
