package io.github.technoir42.gradle

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import java.net.URI

class Environment(private val providerFactory: ProviderFactory) {
    val isCi: Boolean
        get() = providerFactory.environmentVariable("GITHUB_ACTIONS").isPresent

    val repositoryUrl: Provider<URI>
        get() = providerFactory.environmentVariable("GITHUB_SERVER_URL")
            .zip(providerFactory.environmentVariable("GITHUB_REPOSITORY")) { serverUrl, repository ->
                URI(serverUrl).resolve(repository)
            }
}
