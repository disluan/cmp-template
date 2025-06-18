package extensions

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import java.io.File

internal val Project.catalogs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.compose: ComposePlugin.Dependencies
    get() = extensions.getByType<ComposeExtension>().dependencies

internal fun Project.getPluginId(alias: String): String {
    return catalogs.findPlugin(alias).get().get().pluginId
}

internal fun Project.getBundle(alias: String): Provider<ExternalModuleDependencyBundle?> {
    return catalogs.findBundle(alias).get()
}

internal fun Project.getLibrary(alias: String): Provider<MinimalExternalModuleDependency?> {
    return catalogs.findLibrary(alias).get()
}

internal val BaseAppModuleExtension.arrayProguardFiles: Array<Any>
    get() = arrayOf(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )

internal fun Project.configureAndroid(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        compileSdk = findProperty("sdk.compile").toString().toInt()

        defaultConfig {
            minSdk = findProperty("sdk.minimal").toString().toInt()
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}