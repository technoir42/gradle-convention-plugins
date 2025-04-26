package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Property

interface CommonBuildFeatures {
    /**
     * Enable Kotlin serialization. Disabled by default.
     */
    val serialization: Property<Boolean>
}
