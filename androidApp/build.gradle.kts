plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinExtensions)
    kotlin(Plugins.kapt)
}

group = "com.wind.animelist"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
dependencies {
    implementation(project(":shared"))
    implementation(project(":shareAndroid"))

    // kotlin
    implementation(Libs.Kotlin.std)

    // core
    implementation(Libs.Android.appCompat)
    implementation(Libs.Android.material)
    implementation(Libs.Android.recyclerView)
    implementation(Libs.Android.constraint)
    implementation(Libs.Android.core)
    implementation(Libs.Android.fragment)
    implementation(Libs.Android.lifeCycle)
    implementation(Libs.Android.liveData)
    implementation(Libs.Android.viewModel)

    // injection
    implementation(Libs.Injection.android)

    // glide
    implementation(Libs.Glide.glide1)
    kapt(Libs.Glide.glide2)
    implementation(Libs.Glide.glideTransform)

    // thread
    implementation(Libs.Thread.coreAndroid)
}
android {
    compileSdkVersion(Configs.compileSdk)

    defaultConfig {
        applicationId = Configs.androidApplicationId
        minSdkVersion(Configs.minSdk)
        targetSdkVersion(Configs.targetSdk)
        versionCode = Configs.versionCode
        versionName = Configs.versionName
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        jvmTarget = "1.8"
    }
}