import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinExtensions)
    id(Plugins.serialization)
    kotlin(Plugins.kapt)
    kotlin("native.cocoapods")
}

kotlin {
    cocoapods {
        summary = "A cross-platform anime list"
        homepage = "https://github.com/PhongHuynh93/AnimeListKMM"
        ios.deploymentTarget = "13.5"
        podfile = project.file("../iosApp/Podfile")
        // The name of the produced framework can be changed.
        // The name of the Gradle project is used here by default.
        frameworkName = "shared"
    }
    // target 1.8 when using kodein
    // https://github.com/Kodein-Framework/Kodein-Samples/blob/master/di/coffee-maker/common/build.gradle.kts
    android {
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    ios {}
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.Injection.core)
                implementation(Libs.Thread.core)
                implementation(Libs.Network.core)
                implementation(Libs.Network.core2)
                implementation(Libs.Network.parser)
                implementation(Libs.Network.parser2)
                implementation(Libs.Network.logCore)
                implementation(Libs.Network.logCore2)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val commonAndroidMain by creating {
            kotlin.srcDir("src/commonAndroidMain/java")
            resources.srcDir("src/commonAndroidMain/res")

            dependencies {
                // kotlin
                implementation(Libs.Kotlin.std)

                // android lib
                implementation(Libs.Android.appCompat)
                implementation(Libs.Android.material)
                implementation(Libs.Android.recyclerView)

                // kts
                implementation(Libs.Android.core)
                implementation(Libs.Android.fragment)
                implementation(Libs.Android.lifeCycle)
                implementation(Libs.Android.liveData)
                implementation(Libs.Android.viewModel)

                // glide
                implementation(Libs.Glide.glide1)
                implementation(Libs.Glide.glide2)
            }
        }
        val androidMain by getting {
            dependsOn(commonAndroidMain)
            dependencies {
                implementation(Libs.Android.viewModel)
                implementation(Libs.Android.liveData)
                implementation(Libs.Network.android)
                implementation(Libs.Network.logAndroid)
                implementation(Libs.Helper.log)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Libs.Network.ios)
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(Configs.compileSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.setSrcDirs(listOf("src/androidMain/kotlin"))
    sourceSets["main"].res.setSrcDirs(listOf("src/androidMain/res"))

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
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)