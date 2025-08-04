package io.github.technoir42.conventions.jvm.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@JvmLibraryDsl
interface JvmLibraryExtension : CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: JvmBuildFeatures

    fun buildFeatures(action: Action<JvmBuildFeatures>) {
        action.execute(buildFeatures)
    }

    companion object {
        const val NAME = "jvmLibrary"
    }
}
