package io.github.technoir42.conventions.native.application.api

import io.github.technoir42.conventions.common.api.CommonBuildFeatures
import org.gradle.api.provider.Property

interface NativeApplicationBuildFeatures : CommonBuildFeatures {
    /**
     * Enable C interop. Disabled by default.
     */
    val cinterop: Property<Boolean>

    override fun initDefaults() {
        super.initDefaults()
        cinterop.convention(false)
    }
}
