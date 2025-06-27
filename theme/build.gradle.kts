import extensions.addIosTarget

plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
}

kotlin {
    addIosTarget()
    androidLibrary {
        namespace = "com.theme"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.utility)
            api(compose.material3)
            implementation(libs.landscapist)
        }
    }
}