package io.github.technoir42.conventions.kotlin.multiplatform.api

import io.github.technoir42.conventions.common.api.CommonBuildFeatures
import org.gradle.api.provider.Property

interface KotlinMultiplatformBuildFeatures : CommonBuildFeatures {
    /**
     * Enable C interop. Disabled by default.
     */
    val cinterop: Property<Boolean>

    override fun initDefaults() {
        super.initDefaults()
        cinterop.convention(false)
    }
}
