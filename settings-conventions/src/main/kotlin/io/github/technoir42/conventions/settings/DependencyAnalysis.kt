package io.github.technoir42.conventions.settings

import com.autonomousapps.DependencyAnalysisExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure

internal fun Settings.configureDependencyAnalysis() {
    extensions.configure(DependencyAnalysisExtension::class) {
        abi {
            exclusions {
                ignoreGeneratedCode()
                ignoreInternalPackages()
            }
        }
    }
}
