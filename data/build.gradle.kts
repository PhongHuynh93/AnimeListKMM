plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinExtensions)
    kotlin(Plugins.kapt)
    id(Plugins.serialization)
}

android {
    compileSdkVersion(Configs.compileSdk)

    defaultConfig {
        minSdkVersion(Configs.minSdk)
        targetSdkVersion(Configs.targetSdk)
        versionCode = Configs.versionCode
        versionName = Configs.versionName
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.Kotlin.std)

    // dagger
    implementation(Libs.Injection.core)

    // network
    implementation(Libs.Network.network1)
    implementation(Libs.Network.network2)
    implementation(Libs.Network.network3)
    implementation(Libs.Network.log)
    implementation(Libs.Network.log2)
    implementation(Libs.Network.parser)

    // thread
    implementation(Libs.Thread.coroutine)
}