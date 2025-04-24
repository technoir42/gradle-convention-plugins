package io.github.technoir42.conventions.settings.api

import org.gradle.api.provider.Property

interface SettingsExtension {
    /**
     * Unique project ID. Required.
     */
    val projectId: Property<String>

    companion object {
        const val NAME = "globalSettings"
    }
}
