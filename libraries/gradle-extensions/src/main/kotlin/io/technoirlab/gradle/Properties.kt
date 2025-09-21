package io.technoirlab.gradle

import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

fun <T : Any> Property<T>.setDisallowChanges(value: T?) {
    set(value)
    disallowChanges()
}

fun <T : Any> Property<T>.setDisallowChanges(provider: Provider<T>) {
    set(provider)
    disallowChanges()
}
