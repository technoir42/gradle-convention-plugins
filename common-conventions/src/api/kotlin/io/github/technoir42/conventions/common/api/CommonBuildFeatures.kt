package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Property

interface CommonBuildFeatures {
    /**
     * Enable ABI validation. Disabled by default.
     */
    val abiValidation: Property<Boolean>

    /**
     * Generate BuildConfig. Disabled by default.
     */
    val buildConfig: Property<Boolean>

    /**
     * Enable Kotlin serialization. Disabled by default.
     */
    val serialization: Property<Boolean>

    fun initDefaults() {
        abiValidation.convention(false)
        buildConfig.convention(false)
        serialization.convention(false)
    }
}
