import extensions.compose
import extensions.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply(getPluginId(alias = "compose-compiler"))
                apply(getPluginId(alias = "compose-multiplatform"))
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(compose.preview)
                    }
                    commonMain.dependencies {
                        implementation(compose.ui)
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.components.uiToolingPreview)
                    }
                }
            }
        }
    }
}