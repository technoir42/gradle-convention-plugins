package io.github.technoir42.conventions.settings

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.kotlin.dsl.registerIfAbsent
import java.util.concurrent.ConcurrentLinkedDeque

abstract class NmcpProjectCollectorService : BuildService<BuildServiceParameters.None> {
    val projectPaths = ConcurrentLinkedDeque<String>()

    internal companion object {
        fun Project.getProjectCollectorService(): Provider<NmcpProjectCollectorService> =
            gradle.sharedServices.registerIfAbsent("nmcpProjectCollector", NmcpProjectCollectorService::class) {}
    }
}
