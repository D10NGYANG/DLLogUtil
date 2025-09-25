package com.d10ng.log

actual fun platformLog(data: LogEntry) {
    if (data.level == LogLevel.NONE) return
    println(data)
}