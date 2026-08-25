plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Published to GitHub Packages (Phase 5) — phone/watch depend on these coordinates directly
// (gradle/libs.versions.toml's exposuresCommon version) rather than composite-build substitution.
// Bump this version and re-tag (triggers .github/workflows/publish.yml) for any new release.
allprojects {
    group = "com.exposures.common"
    version = "0.8.0"
}
