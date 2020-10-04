buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPaths.gradlePlugin)
        classpath(ClassPaths.kotlinPlugin)
        classpath(ClassPaths.serialization)
    }
}
group = "com.wind.animelist"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
