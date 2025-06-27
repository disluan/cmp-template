import extensions.addIosTarget

plugins {
    alias(libs.plugins.convention.library)
}

kotlin {
    addIosTarget()
    androidLibrary {
        namespace = "com.utility"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}