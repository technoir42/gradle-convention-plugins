package io.github.technoir42.conventions.native.application.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@NativeApplicationDsl
interface NativeApplicationExtension : CommonExtension {
    /**
     * Whether to create default targets.
     */
    val defaultTargets: Property<Boolean>

    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: NativeApplicationBuildFeatures

    fun buildFeatures(action: Action<NativeApplicationBuildFeatures>) {
        action.execute(buildFeatures)
    }

    override fun initDefaults(projectName: String) {
        super.initDefaults(projectName)
        defaultTargets.convention(true)
    }

    companion object {
        const val NAME = "nativeApplication"
    }
}
