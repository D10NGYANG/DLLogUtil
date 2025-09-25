package com.d10ng.log

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * 日志数据
 * @Author d10ng
 * @Date 2025/9/25 15:27
 */
@OptIn(ExperimentalTime::class)
data class LogEntry(
    val tag: String,
    val level: LogLevel,
    val message: String,
    val time: Long = Clock.System.now().toEpochMilliseconds(),
) {
    override fun toString(): String {
        return "${level.name} [$tag] $message"
    }
}
