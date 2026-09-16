import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "ir.neobank.ariapay.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}


tasks {
    validatePlugins {
        enableStricterValidation.set(true)
        failOnWarning.set(true)
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "ir.neobank.ariapay.android.application"
            implementationClass = "ir.neobank.ariapay.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "ir.neobank.ariapay.android.library"
            implementationClass = "ir.neobank.ariapay.convention.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "ir.neobank.ariapay.android.feature"
            implementationClass = "ir.neobank.ariapay.convention.AndroidFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "ir.neobank.ariapay.android.compose"
            implementationClass = "ir.neobank.ariapay.convention.AndroidComposeConventionPlugin"
        }
        register("hilt") {
            id = "ir.neobank.ariapay.hilt"
            implementationClass = "ir.neobank.ariapay.convention.HiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = "ir.neobank.ariapay.jvm.library"
            implementationClass = "ir.neobank.ariapay.convention.JvmLibraryConventionPlugin"
        }
    }
}
