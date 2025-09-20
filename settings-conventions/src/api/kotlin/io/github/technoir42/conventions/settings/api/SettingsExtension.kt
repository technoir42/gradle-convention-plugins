package io.github.technoir42.conventions.settings.api

import io.github.technoir42.conventions.common.api.metadata.ProjectMetadata
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@SettingsDsl
interface SettingsExtension {
    /**
     * Unique project ID. Required.
     */
    val projectId: Property<String>

    /**
     * Information about the project.
     */
    @get:Nested
    val metadata: ProjectMetadata

    /**
     * Information about the project.
     */
    fun metadata(configure: Action<ProjectMetadata>) {
        configure.execute(metadata)
    }

    companion object {
        const val NAME = "globalSettings"
    }
}
