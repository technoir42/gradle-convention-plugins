package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Property

interface CommonBuildFeatures {
    /**
     * Generate BuildConfig. Disabled by default.
     */
    val buildConfig: Property<Boolean>

    /**
     * Enable Kotlin serialization. Disabled by default.
     */
    val serialization: Property<Boolean>
}
