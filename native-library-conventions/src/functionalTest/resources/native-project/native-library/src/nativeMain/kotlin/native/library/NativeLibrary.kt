package native.library

import kotlinx.cinterop.ExperimentalForeignApi

@ExperimentalForeignApi
fun greet(name: String) {
    nativeGreet(name)
}
