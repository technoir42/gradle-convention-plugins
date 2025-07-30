package io.github.technoir42.conventions.native.library.api

import io.github.technoir42.conventions.common.api.CommonBuildFeatures
import org.gradle.api.provider.Property

interface NativeLibraryBuildFeatures : CommonBuildFeatures {
    /**
     * Enable C interop. Disabled by default.
     */
    val cinterop: Property<Boolean>

    override fun initDefaults() {
        super.initDefaults()
        cinterop.convention(false)
    }
}
