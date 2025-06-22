import extensions.addIosTarget

plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    addIosTarget()
    androidLibrary {
        namespace = "com.core"
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.okhttp)
        }
        commonMain.dependencies {
            api(project(":firebase"))
            implementation(libs.koin.core)
            implementation(libs.bundles.ktor)
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
        }
    }
}