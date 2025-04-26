package io.github.technoir42.conventions.common.api

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.internal.extensions.core.extra

class ProjectSettings(private val project: Project) {
    val groupId: Provider<String>
        get() = project.localGradleProperty("project.groupId").orElse(DEFAULT_GROUP_ID)

    val version: Provider<String>
        get() = project.localGradleProperty("project.version").orElse(DEFAULT_VERSION)

    private fun Project.localGradleProperty(propertyName: String): Provider<String> =
        provider { if (extra.has(propertyName)) extra.get(propertyName) as String? else null }

    companion object {
        const val DEFAULT_GROUP_ID = "io.github.technoir42"
        const val DEFAULT_VERSION = "dev"
    }
}
