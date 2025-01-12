import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
    id("com.codingfeline.buildkonfig")
}

kotlin {

    applyDefaultHierarchyTemplate()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.startup.runtime)

            api(libs.androidx.activity.compose)
            api(libs.androidx.appcompat)
            api(libs.androidx.core.ktx)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.koin.android)

            implementation(libs.sqldelight.android.driver)
            implementation(libs.sqlite.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            implementation(libs.stately.common)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.core)

            api(libs.koin.core)
            api(libs.koin.compose)

            implementation(libs.kermit)

            implementation(libs.sqldelight.coroutines.extensions)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)

            implementation(libs.sqldelight.ios.driver)
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

buildkonfig {
    packageName = "com.santimattius.kmp.skeleton"
    objectName = "BuildConfig"

    defaultConfigs {
        buildConfigField(STRING, "apiKey", getLocalProperty("apiKey"))
    }
}

sqldelight {
    databases {
        create("MoviesDatabase") {
            packageName.set("com.santimattius.kmp.skeleton")
            dialect("app.cash.sqldelight:sqlite-3-38-dialect:2.0.2")
        }
    }

    linkSqlite.set(true)

}

android {
    namespace = "com.santimattius.kmp.compose.skeleton"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.santimattius.kmp.compose.skeleton"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

