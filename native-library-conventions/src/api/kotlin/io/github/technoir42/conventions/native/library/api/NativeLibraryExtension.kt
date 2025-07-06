package io.github.technoir42.conventions.native.library.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@NativeLibraryDsl
interface NativeLibraryExtension : CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: NativeLibraryBuildFeatures

    fun buildFeatures(action: Action<NativeLibraryBuildFeatures>) {
        action.execute(buildFeatures)
    }

    companion object {
        const val NAME = "nativeLibrary"
    }
}
