plugins {
    `kotlin-dsl`
}

group = "com.buildlogic"

dependencies {
    compileOnly(libs.gradle.android)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.compose)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.convention.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        create("composeMultiplatform") {
            id = "com.convention.compose"
            implementationClass = "ComposeMultiplatformPlugin"
        }
        create("kotlinMultiplatformLibrary") {
            id = "com.convention.library"
            implementationClass = "KotlinMultiplatformLibraryPlugin"
        }
    }
}