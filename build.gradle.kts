plugins {
    `kotlin-dsl` apply false
    id("io.technoirlab.conventions.root")
}

dependencies {
    dokka(project(":common-conventions"))
    dokka(project(":gradle-plugin-conventions"))
    dokka(project(":jvm-conventions"))
    dokka(project(":kotlin-multiplatform-conventions"))
    dokka(project(":libraries:gradle-extensions"))
    dokka(project(":libraries:gradle-test-kit"))
    dokka(project(":root-conventions"))
    dokka(project(":settings-conventions"))
}
