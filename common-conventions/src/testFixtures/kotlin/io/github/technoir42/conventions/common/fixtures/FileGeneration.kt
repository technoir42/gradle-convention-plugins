package io.github.technoir42.conventions.common.fixtures

import java.io.File

@Suppress("SpreadOperator")
fun File.createDependencyGraph(packageName: String = "com.example") {
    val dependencyGraphFile = resolve(
        "src",
        "commonMain",
        "kotlin",
        *packageName.split(".").toTypedArray(),
        "AppGraph.kt"
    )
    dependencyGraphFile.parentFile.mkdirs()

    dependencyGraphFile.writeText(
        //language=kotlin
        """
            package $packageName
            
            import dev.zacsweers.metro.AppScope
            import dev.zacsweers.metro.DependencyGraph
            import dev.zacsweers.metro.Provides
            
            @DependencyGraph(AppScope::class)
            interface AppGraph {
                @Provides
                fun provideString(): String = "hello"
            }
        """.trimIndent()
    )
}
