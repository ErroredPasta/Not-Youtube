pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Not Youtube"
include(":app")
include(":core:core-util")
include(":core:core-ui")
include(":video:video-domain")
include(":video:video-data")
include(":video:video-ui")
