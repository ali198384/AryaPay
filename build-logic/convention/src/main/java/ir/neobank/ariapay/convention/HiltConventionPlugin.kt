package ir.neobank.ariapay.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            val isAndroid = pluginManager.hasPlugin("com.android.application") ||
                    pluginManager.hasPlugin("com.android.library")

            if (isAndroid) {
                pluginManager.apply("com.google.dagger.hilt.android")
                dependencies {
                    add("implementation", libs.findLibrary("hilt-android").get())
                    add("ksp", libs.findLibrary("hilt-compiler").get())
                }
            } else {
                dependencies {
                    add("implementation", libs.findLibrary("javax-inject").get())
                    add("implementation", libs.findLibrary("hilt-core").get())
                    add("ksp", libs.findLibrary("hilt-compiler").get())
                }
            }
        }
    }
}
