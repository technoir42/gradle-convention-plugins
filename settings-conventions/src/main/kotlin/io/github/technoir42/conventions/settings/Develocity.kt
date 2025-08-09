package io.github.technoir42.conventions.settings

import io.github.technoir42.gradle.Environment
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

internal fun Settings.configureDevelocity(environment: Environment) {
    develocity {
        buildScan {
            val isCi = environment.isCi
            publishing.onlyIf { false }
            uploadInBackground.set(!isCi)

            if (isCi) {
                tag("CI")
            } else {
                tag("Local")
            }

            termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
            termsOfUseAgree.set("yes")
        }
    }
}
