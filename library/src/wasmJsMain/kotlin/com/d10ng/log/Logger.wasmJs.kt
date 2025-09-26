@file:OptIn(ExperimentalWasmJsInterop::class)

package com.d10ng.log

external interface Console : JsAny {
    fun log(vararg args: String?)
    fun info(vararg args: String?)
    fun warn(vararg args: String?)
    fun error(vararg args: String?)
}

external val console: Console

actual fun platformLog(data: LogEntry) {
    when (data.level) {
        LogLevel.VERBOSE, LogLevel.DEBUG -> console.log(data.toString())
        LogLevel.INFO -> console.info(data.toString())
        LogLevel.WARN -> console.warn(data.toString())
        LogLevel.ERROR -> console.error(data.toString())
        else -> {}
    }
}