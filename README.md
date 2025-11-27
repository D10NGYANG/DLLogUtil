# DLLogUtil

轻量级 Kotlin Multiplatform 日志库，支持在多个平台上统一采集与打印日志，便于开发与调试。

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet?logo=kotlin&logoColor=white)](#)
[![Version](https://img.shields.io/badge/version-0.1.2-blue)](#) 

## 支持平台

- ![Android](https://img.shields.io/badge/Android-✅-black?logo=android)
- ![JVM](https://img.shields.io/badge/JVM-✅-black?logo=java)
- ![JavaScript (Browser/Node)](https://img.shields.io/badge/JS%20(Browser%2FNode)-✅-black?logo=javascript)
- ![Wasm JS (Browser)](https://img.shields.io/badge/Wasm%20JS%20(Browser)-✅-black)
- ![Wasm WASI (Node)](https://img.shields.io/badge/Wasm%20WASI%20(Node)-✅-black)
- ![Linux x64/arm64](https://img.shields.io/badge/Linux%20x64%2Farm64-✅-black?logo=linux)
- ![macOS x64/arm64](https://img.shields.io/badge/macOS%20x64%2Farm64-✅-black?logo=apple)
- ![iOS Arm64/Sim Arm64/x64](https://img.shields.io/badge/iOS%20Arm64%2FSim%20Arm64%2Fx64-✅-black?logo=apple)

## 安装

在你的 KMP 项目中添加仓库与依赖（Kotlin DSL）：

```kotlin
// settings.gradle.kts 或 build.gradle.kts 中的仓库（推荐）
dependencyResolutionManagement {
    repositories {
        maven("https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository") {
          mavenContent {
            includeGroupAndSubgroups("com.github.D10NGYANG")
          }
        }
        google()
        mavenCentral()
    }
}
```

然后在模块的 `build.gradle.kts` 中添加依赖（建议在 `commonMain`）：

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.D10NGYANG:DLLogUtil:0.1.2")
            }
        }
    }
}
```

## 使用说明

### 获取 Logger 并设置日志等级

```kotlin
import com.d10ng.log.LoggerFactory
import com.d10ng.log.LogLevel

val logger = LoggerFactory.create("App")

// 设置单个标签的最小日志等级（默认 NONE，不会打印到平台）
LoggerFactory.setLogLevel("App", LogLevel.DEBUG)

// 或设置全局最小日志等级
LoggerFactory.setGlobalLogLevel(LogLevel.INFO)
```

### 打印日志

```kotlin
logger.v { "Verbose message" }
logger.d { "Debug message" }
logger.i { "Info message" }
logger.w { "Warn message" }
logger.e { "Error message" }
```

说明：
- 当某标签的最小日志等级未设置或为 `NONE` 时，日志不会打印到平台，但如果存在订阅者，日志仍会推送到数据流。
- 实际的平台打印由各平台的 `platformLog(LogEntry)` 实现完成（Android 可打印到 Logcat，JVM/JS 可打印到控制台等）。

### 订阅日志数据流（跨平台采集）

```kotlin
import com.d10ng.log.LoggerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

val scope = CoroutineScope(Dispatchers.Default)
val job = scope.launch {
    LoggerFactory.logDataFlow.collect { entry ->
        // entry: LogEntry(tag, level, message, time)
        println("${entry.time} ${entry}")
    }
}
```

### 其他 API

```kotlin
// 获取指定 Logger
val l: com.d10ng.log.Logger? = LoggerFactory.get("App")

// 获取所有 Logger
val all: Map<String, com.d10ng.log.Logger> = LoggerFactory.getAll()

// 移除 Logger
LoggerFactory.remove("App")

// 查询某标签的当前最小日志等级
val level: com.d10ng.log.LogLevel? = LoggerFactory.getLogLevel("App")
```

## 设计要点

- 统一入口：`LoggerFactory` 创建/管理 Logger，并提供跨平台 `logDataFlow` 供订阅
- 高性能：`MutableSharedFlow` 使用超大缓冲（extraBufferCapacity = Int.MAX_VALUE），尽量减少背压丢失
- 平台适配：通过 `expect fun platformLog(data: LogEntry)` 提供平台端打印实现
- 易用性：DSL 风格日志方法（`v/d/i/w/e { ... }`）延迟计算消息，只有需要时才构造字符串
