import extensions.addIosTarget

plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.kotlin.cocoapods)
}

kotlin {
    addIosTarget()
    androidLibrary {
        namespace = "com.firebase"
    }

    cocoapods {
        version = "1.0"
        summary = "Firebase App"
        homepage = "https://github.com/disluan/cmp-template"
        ios.deploymentTarget = "16.0"

        framework {
            baseName = "FirebaseKit"
        }

        pod("FirebaseCore") {
            version = "~> 11.13"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod("FirebaseAuth") {
            version = "~> 11.13"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.android.firebase.bom))
            implementation(libs.bundles.android.firebase)
            implementation(libs.androidx.startup)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines)
        }
    }
}