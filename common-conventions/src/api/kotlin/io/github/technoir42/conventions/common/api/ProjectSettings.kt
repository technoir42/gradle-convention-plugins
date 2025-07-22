package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Provider

interface ProjectSettings {
    val groupId: Provider<String>
    val version: Provider<String>

    companion object {
        const val DEFAULT_GROUP_ID = "io.github.technoir42"
        const val DEFAULT_VERSION = "dev"
    }
}
