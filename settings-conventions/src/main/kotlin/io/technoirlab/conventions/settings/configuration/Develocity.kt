package io.technoirlab.conventions.settings.configuration

import io.technoirlab.conventions.settings.api.SettingsExtension
import io.technoirlab.gradle.Environment
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

internal fun Settings.configureDevelocity(config: SettingsExtension, environment: Environment) {
    develocity {
        projectId.set(config.projectId)
        server.set(config.develocityUrl.map { it.toString() })

        buildScan {
            val isCi = environment.isCi
            publishing.onlyIf { config.develocityUrl.isPresent }
            uploadInBackground.set(!isCi)

            if (isCi) {
                tag("CI")
            } else {
                tag("Local")
            }

            if (environment.isLaunchedFromTest) {
                tag("Test")
            }

            if (isCi) {
                termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
                termsOfUseAgree.set("yes")
            }
        }
    }
}
