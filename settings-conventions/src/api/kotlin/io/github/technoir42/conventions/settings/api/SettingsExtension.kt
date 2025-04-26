package io.github.technoir42.conventions.settings.api

import org.gradle.api.provider.Property

@SettingsDsl
interface SettingsExtension {
    /**
     * Unique project ID. Required.
     */
    val projectId: Property<String>

    companion object {
        const val NAME = "globalSettings"
    }
}
