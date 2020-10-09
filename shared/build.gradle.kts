import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id(Plugins.androidLibrary)
    kotlin(Plugins.kotlinExtensions)
    id(Plugins.serialization)
    kotlin(Plugins.kapt)
}

kotlin {
    // target 1.8 when using kodein
    // https://github.com/Kodein-Framework/Kodein-Samples/blob/master/di/coffee-maker/common/build.gradle.kts
    android {
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.Injection.core)
                implementation(Libs.Thread.core)
                implementation(Libs.Network.core)
                implementation(Libs.Network.core2)
                implementation(Libs.Network.parserCore)
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
        val androidMain by getting {
            dependencies {
                implementation(Libs.Injection.android)
                implementation(Libs.Android.viewModel)
                implementation(Libs.Android.liveData)
                implementation(Libs.Network.android)
                implementation(Libs.Network.parserAndroid)
                implementation(Libs.Network.logAndroid)
                implementation(Libs.Thread.coreAndroid)
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
                implementation(Libs.Network.parserIos)
                implementation(Libs.Network.logIos)
                implementation(Libs.Thread.coreIos)
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