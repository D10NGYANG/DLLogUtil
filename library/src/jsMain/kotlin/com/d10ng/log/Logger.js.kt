package com.d10ng.log

actual fun platformLog(data: LogEntry) {
    when (data.level) {
        LogLevel.VERBOSE, LogLevel.DEBUG -> console.log(data.toString())
        LogLevel.INFO -> console.info(data.toString())
        LogLevel.WARN -> console.warn(data.toString())
        LogLevel.ERROR -> console.error(data.toString())
        else -> {}
    }
}