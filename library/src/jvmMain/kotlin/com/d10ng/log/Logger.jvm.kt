package com.d10ng.log

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault()) // 使用系统默认时区
    val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS")
    return zonedDateTime.format(formatter)
}

actual fun platformLog(data: LogEntry) {
    if (data.level == LogLevel.NONE) return
    println("[${formatTimestamp(data.time)}] $data")
}