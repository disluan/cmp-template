package extensions

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.addIosTarget() : List<KotlinNativeTarget> {
    val list = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    return list
}