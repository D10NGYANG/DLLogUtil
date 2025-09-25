package com.d10ng.log

/**
 * 日志
 * @Author d10ng
 * @Date 2025/9/25 15:13
 */
class Logger internal constructor(
    val tag: String
) {

    // 最小日志等级
    var miniLevel: LogLevel = LogLevel.NONE

    /**
     * 日志打印
     * @param level 日志等级
     * @param msg 日志信息
     */
    fun log(level: LogLevel, msg: () -> String) {
        // 当有订阅者时，或者日志等级大于等于最小日志等级时，打印日志
        val hasSub = LoggerFactory.logDataFlow.subscriptionCount.value > 0
        val canLog = level >= miniLevel
        val message = if (hasSub || canLog) msg() else null
        if (message == null) return
        val data = LogEntry(tag, level, message)
        if (canLog) platformLog(data)
        if (hasSub) LoggerFactory.emit(data)
    }

    fun v(msg: () -> String) {
        log(LogLevel.VERBOSE, msg)
    }

    fun d(msg: () -> String) {
        log(LogLevel.DEBUG, msg)
    }

    fun i(msg: () -> String) {
        log(LogLevel.INFO, msg)
    }

    fun w(msg: () -> String) {
        log(LogLevel.WARN, msg)
    }

    fun e(msg: () -> String) {
        log(LogLevel.ERROR, msg)
    }
}

/**
 * 日志打印
 * @param data 日志数据
 */
expect fun platformLog(data: LogEntry)