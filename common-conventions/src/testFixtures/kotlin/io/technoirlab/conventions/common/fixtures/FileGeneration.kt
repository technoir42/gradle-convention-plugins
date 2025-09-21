package io.technoirlab.conventions.common.fixtures

import kotlin.io.path.createParentDirectories
import kotlin.io.path.writeText

@Suppress("SpreadOperator")
fun GradleProject.createDependencyGraph(className: String = "com.example.AppGraph") = apply {
    val packageName = className.substringBeforeLast('.')
    val classNameWithoutPackage = className.substringAfterLast('.')
    kotlinFile(className, variant = "commonMain")
        .createParentDirectories()
        .writeText(
            // language=kotlin
            """
                package $packageName
                
                import dev.zacsweers.metro.AppScope
                import dev.zacsweers.metro.DependencyGraph
                import dev.zacsweers.metro.Provides
                import dev.zacsweers.metro.createGraph
                
                @DependencyGraph(AppScope::class)
                interface $classNameWithoutPackage {
                    @Provides
                    fun provideString(): String = "hello"
                }
                
                val graph = createGraph<AppGraph>()
            """.trimIndent()
        )
}
