plugins {
    id("io.technoirlab.conventions.jvm-library")
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

samWithReceiver {
    annotation(HasImplicitReceiver::class.qualifiedName!!)
}
