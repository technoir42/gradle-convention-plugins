package native.library

import kotlinx.cinterop.ExperimentalForeignApi
import lib.nativeGreet

@ExperimentalForeignApi
fun greet(name: String) {
    nativeGreet(name)
}
