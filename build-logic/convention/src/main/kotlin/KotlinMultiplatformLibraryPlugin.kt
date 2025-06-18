import com.android.build.api.dsl.androidLibrary
import extensions.addIosTarget
import extensions.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(getPluginId(alias = "kotlin-multiplatform"))
                apply(getPluginId(alias = "kotlin-multiplatform-library"))
            }

            extensions.configure<KotlinMultiplatformExtension> {
                addIosTarget()
                androidLibrary {
                    compileSdk = findProperty("sdk.compile").toString().toInt()
                    minSdk = findProperty("sdk.minimal").toString().toInt()
                }

                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                    freeCompilerArgs.add("-opt-in=kotlinx.cinterop.ExperimentalForeignApi")
                }
            }
        }
    }
}