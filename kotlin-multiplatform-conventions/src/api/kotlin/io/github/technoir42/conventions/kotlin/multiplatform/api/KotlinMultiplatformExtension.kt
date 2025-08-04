package io.github.technoir42.conventions.kotlin.multiplatform.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

interface KotlinMultiplatformExtension : CommonExtension {
    /**
     * Whether to create default targets.
     */
    val defaultTargets: Property<Boolean>

    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: KotlinMultiplatformBuildFeatures

    fun buildFeatures(action: Action<KotlinMultiplatformBuildFeatures>) {
        action.execute(buildFeatures)
    }

    override fun initDefaults(projectName: String) {
        super.initDefaults(projectName)
        defaultTargets.convention(true)
    }
}
