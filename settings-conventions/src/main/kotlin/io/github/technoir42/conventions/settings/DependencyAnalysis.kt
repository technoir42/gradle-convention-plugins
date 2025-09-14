package io.github.technoir42.conventions.settings

import com.autonomousapps.DependencyAnalysisExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure

internal fun Settings.configureDependencyAnalysis() {
    extensions.configure(DependencyAnalysisExtension::class) {
        structure {
            bundle("junit-jupiter") {
                primary("org.junit.jupiter:junit-jupiter")
                include("org.junit.jupiter:junit-jupiter-api")
                include("org.junit.jupiter:junit-jupiter-params")
            }
        }
        issues {
            all {
                onUnusedDependencies {
                    exclude("org.junit.jupiter:junit-jupiter")
                }
            }
        }
        abi {
            exclusions {
                ignoreGeneratedCode()
                ignoreInternalPackages()
            }
        }
    }
}
