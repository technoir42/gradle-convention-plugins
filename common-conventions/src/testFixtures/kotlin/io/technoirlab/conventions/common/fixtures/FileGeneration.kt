package io.technoirlab.conventions.common.fixtures

import kotlin.io.path.createParentDirectories
import kotlin.io.path.writeText

@Suppress("SpreadOperator")
fun GradleProject.createDependencyGraph(className: String = "com.example.AppGraph") {
    val packageName = className.substringBeforeLast('.')
    val classNameWithoutPackage = className.substringAfterLast('.')
    kotlinFile(className, variant = "commonMain")
        .createParentDirectories()
        .writeText(
            //language=kotlin
            """
                package $packageName
                
                import dev.zacsweers.metro.AppScope
                import dev.zacsweers.metro.DependencyGraph
                import dev.zacsweers.metro.Provides
                
                @DependencyGraph(AppScope::class)
                interface $classNameWithoutPackage {
                    @Provides
                    fun provideString(): String = "hello"
                }
            """.trimIndent()
        )
}
