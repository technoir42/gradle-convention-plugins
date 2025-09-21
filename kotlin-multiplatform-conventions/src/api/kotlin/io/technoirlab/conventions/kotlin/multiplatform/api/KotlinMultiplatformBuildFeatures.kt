package io.technoirlab.conventions.kotlin.multiplatform.api

import io.technoirlab.conventions.common.api.CommonBuildFeatures
import org.gradle.api.provider.Property

interface KotlinMultiplatformBuildFeatures : CommonBuildFeatures {
    /**
     * Enable C interop. Disabled by default.
     */
    val cinterop: Property<Boolean>

    /**
     * Enable Metro dependency injection.
     */
    val metro: Property<Boolean>

    override fun initDefaults() {
        super.initDefaults()
        cinterop.convention(false)
        metro.convention(false)
    }
}
