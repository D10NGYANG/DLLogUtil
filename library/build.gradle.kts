@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "0.2.1"

kotlin {
    withSourcesJar(publish = true)

    jvmToolchain(8)
    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
        publishLibraryVariants("release")
    }
    jvm()
    js {
        browser()
        nodejs()
        binaries.library()
    }
    wasmJs {
        browser()
        binaries.library()
    }
    wasmWasi {
        nodejs()
        binaries.library()
    }
    linuxX64()
    linuxArm64()
    macosX64()
    macosArm64()
    iosArm64()
    iosSimulatorArm64()
    iosX64()

    sourceSets {
        commonMain.dependencies {
            // 协程
            implementation(libs.kotlinx.coroutines)
        }
    }
}

android {
    namespace = "com.d10ng.log"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

afterEvaluate {
    publishing {
        repositories {
            maven {
                url = uri("/Users/d10ng/project/kotlin/maven-repo/repository")
            }
            maven {
                credentials {
                    username = bds100MavenUsername
                    password = bds100MavenPassword
                }
                setUrl("https://nexus.bds100.com/repository/maven-releases/")
            }
        }
    }
}