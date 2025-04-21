plugins {
    `kotlin-dsl` apply false
}

subprojects {
    pluginManager.withPlugin("java") {
        pluginManager.apply("maven-publish")

        group = providers.gradleProperty("project.groupId").get()
        version = providers.gradleProperty("project.version").get()

        configure<PublishingExtension> {
            repositories {
                maven(providers.gradleProperty("publishRepositoryUrl")) {
                    credentials(PasswordCredentials::class)
                }
            }
        }
    }
}
