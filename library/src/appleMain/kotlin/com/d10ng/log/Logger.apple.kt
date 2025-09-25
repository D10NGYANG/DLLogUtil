package com.d10ng.log

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLog

private val dateFormatter = NSDateFormatter().apply {
    dateFormat = "MM-dd HH:mm:ss.SSS"
}

private fun formatTimestamp(timestamp: Long): String {
    return dateFormatter.stringFromDate(NSDate(timeIntervalSinceReferenceDate = timestamp / 1000.0))
}

private enum class AppleLogLevel(val level: LogLevel, val text: String) {
    VERBOSE(LogLevel.VERBOSE, "💜 VERBOSE"),
    DEBUG(LogLevel.DEBUG, "💚 DEBUG"),
    INFO(LogLevel.INFO, "💙 INFO"),
    WARN(LogLevel.WARN, "💛 WARN"),
    ERROR(LogLevel.ERROR, "❤️ ERROR");
    companion object {
        fun from(level: LogLevel): AppleLogLevel? {
            return entries.firstOrNull { it.level == level }
        }
    }
}

actual fun platformLog(data: LogEntry) {
    if (data.level == LogLevel.NONE) return
    val level = AppleLogLevel.from(data.level) ?: return
    NSLog("[${formatTimestamp(data.time)}] ${level.text} [${data.tag}] ${data.message}")
}