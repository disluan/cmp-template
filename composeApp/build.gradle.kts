import extensions.addIosTarget

plugins {
    alias(libs.plugins.convention.application)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.google.services)
}

kotlin {
    addIosTarget().forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            freeCompilerArgs += "-Xbinary=bundleId=${findProperty("app.id")}"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":firebase"))
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.runtimeCompose)
        }
    }
}

android {
    namespace = "com.sources"

    defaultConfig {
        applicationId = findProperty("app.id").toString()
    }
    buildTypes {
        getByName("release") {
            manifestPlaceholders["application_name"] = "Template"
        }
        getByName("debug") {
            manifestPlaceholders["application_name"] = "Template Debug"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}