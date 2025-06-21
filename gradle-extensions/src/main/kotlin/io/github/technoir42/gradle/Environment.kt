package io.github.technoir42.gradle

import org.gradle.api.provider.ProviderFactory

class Environment(private val providerFactory: ProviderFactory) {
    val isCi: Boolean
        get() = providerFactory.environmentVariable("GITHUB_ACTIONS").isPresent
}
