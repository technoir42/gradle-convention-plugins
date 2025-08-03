package io.github.technoir42.conventions.native.library.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@NativeLibraryDsl
interface NativeLibraryExtension : CommonExtension {
    /**
     * Whether to create default targets.
     */
    val defaultTargets: Property<Boolean>

    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: NativeLibraryBuildFeatures

    fun buildFeatures(action: Action<NativeLibraryBuildFeatures>) {
        action.execute(buildFeatures)
    }

    override fun initDefaults(projectName: String) {
        super.initDefaults(projectName)
        defaultTargets.convention(true)
    }

    companion object {
        const val NAME = "nativeLibrary"
    }
}
