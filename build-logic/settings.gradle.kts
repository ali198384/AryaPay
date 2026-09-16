pluginManagement {
    repositories {
        /*google()
        mavenCentral()
        gradlePluginPortal()*/
        maven { url = uri("https://maven.myket.ir/") }
    }
}

dependencyResolutionManagement {
    repositories {
        /*google()
        mavenCentral()
        gradlePluginPortal()*/
        maven { url = uri("https://maven.myket.ir/") }
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
