pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Swiss Army"
include(":app")
include(":core:core-common")
include(":core:core-ui")
include(":core:core-navigation")
include(":feature:feature-home")
include(":feature:feature-scanner")
include(":feature:feature-pdf-tools")
include(":feature:feature-image-tools")
include(":feature:feature-calculators")
include(":feature:feature-utilities")
include(":feature:feature-dictionary")
include(":feature:feature-settings")
