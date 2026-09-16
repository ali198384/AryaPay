pluginManagement {
    includeBuild("build-logic")
    repositories {
        /*google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()*/

        maven { url = uri("https://maven.myket.ir/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        /*google()
        mavenCentral()*/

        maven { url = uri("https://maven.myket.ir/") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AriaPay"
include(":app")
include(":core:common")
include(":core:model")
include(":feature:home")