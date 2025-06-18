import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import extensions.arrayProguardFiles
import extensions.configureAndroid
import extensions.getBundle
import extensions.getLibrary
import extensions.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(getPluginId(alias = "android-application"))
                apply(getPluginId(alias = "kotlin-multiplatform"))
            }

            extensions.configure<BaseAppModuleExtension> {
                configureAndroid(extension = this)

                defaultConfig {
                    targetSdk = findProperty("sdk.compile").toString().toInt()
                    versionCode = findProperty("version.code").toString().toInt()
                    versionName = findProperty("version.name").toString()
                }

                buildTypes {
                    getByName("release") {
                        multiDexEnabled = true
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(files = arrayProguardFiles)
                    }
                    getByName("debug") {
                        applicationIdSuffix = ".debug"
                    }
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }
                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(getLibrary("activity-compose"))
                    }
                    commonMain.dependencies {
                        implementation(getBundle("koin"))
                    }
                }
            }
        }
    }
}