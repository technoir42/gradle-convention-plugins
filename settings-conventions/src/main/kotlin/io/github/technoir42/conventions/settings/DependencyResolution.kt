package io.github.technoir42.conventions.settings

import io.github.technoir42.conventions.common.Environment
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.RepositoriesMode
import org.gradle.api.initialization.resolve.RulesMode
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.maven

@Suppress("UnstableApiUsage")
internal fun Settings.configureDependencyResolution(environment: Environment) {
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        rulesMode.set(RulesMode.FAIL_ON_PROJECT_RULES)

        repositories {
            mavenCentral()
            maven(providers.gradleProperty("mavenRepositoryUrl")) {
                if (url.scheme != "file") {
                    credentials(PasswordCredentials::class)
                }
            }
            if (!environment.isCi) {
                mavenLocal()
            }
        }
    }
}
