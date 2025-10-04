package io.technoirlab.conventions.common

import io.technoirlab.gradle.test.kit.GradleRunnerExtension
import io.technoirlab.gradle.test.kit.appendBuildScript
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.io.path.div

class CommonConventionPluginFunctionalTest {
    @RegisterExtension
    private val gradleRunner = GradleRunnerExtension("test-project")

    @Test
    fun `sorting dependencies`() {
        val project = gradleRunner.root.project("library")
            .appendBuildScript(
                """
                    dependencies {
                        implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.20")
                        api("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                    }
                """.trimIndent()
            )

        gradleRunner.build(":library:sortDependencies")

        assertThat(project.dir / "build.gradle.kts")
            .content()
            .contains(
                // language=kotlin
                """
                    dependencies {
                        api("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                    
                        implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.20")
                    }
                """.trimIndent()
            )
    }
}
