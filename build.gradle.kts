plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Coordinates consumers substitute against via Gradle composite build (includeBuild +
// dependencySubstitution) in Phase 3 — see phone/watch settings.gradle.kts. Not yet published to
// any repository; substitution matches on group:artifact regardless of version, but the version
// is still pinned here so consumer version catalogs have one real value to reference.
allprojects {
    group = "com.exposures.common"
    version = "0.1.0-SNAPSHOT"
}
