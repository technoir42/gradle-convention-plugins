package io.github.technoir42.conventions.jvm.library.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@JvmLibraryDsl
interface JvmLibraryExtension : CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: JvmLibraryBuildFeatures

    fun buildFeatures(action: Action<JvmLibraryBuildFeatures>) {
        action.execute(buildFeatures)
    }

    companion object {
        const val NAME = "jvmLibrary"
    }
}
