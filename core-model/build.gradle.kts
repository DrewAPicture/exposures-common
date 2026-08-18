plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

dependencies {
    testImplementation(libs.junit4)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            artifactId = "core-model"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DrewAPicture/exposures-common")
            credentials {
                // GITHUB_TOKEN/GITHUB_ACTOR in CI (publish.yml); gpr.token/gpr.user project
                // properties (e.g. ~/.gradle/gradle.properties, never committed) for a local publish.
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
