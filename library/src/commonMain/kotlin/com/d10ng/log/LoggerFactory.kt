package com.d10ng.log

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 日志工厂
 * @Author d10ng
 * @Date 2025/9/25 15:12
 */
object LoggerFactory {

    // 日志数据流
    private val _logDataFlow = MutableSharedFlow<LogEntry>(extraBufferCapacity = Int.MAX_VALUE)
    val logDataFlow = _logDataFlow

    // Logger列表
    private val loggers = mutableMapOf<String, Logger>()

    /**
     * 发送日志数据
     * @param data 日志数据
     */
    internal fun emit(data: LogEntry) {
        _logDataFlow.tryEmit(data)
    }

    /**
     * 创建Logger
     * @param tag 标签
     * @return Logger
     */
    fun create(tag: String): Logger {
        return loggers.getOrPut(tag) {
            Logger(tag)
        }
    }

    /**
     * 获取Logger
     * @param tag 标签
     * @return Logger?
     */
    fun get(tag: String): Logger? {
        return loggers[tag]
    }

    /**
     * 移除Logger
     * @param tag 标签
     */
    fun remove(tag: String) {
        loggers.remove(tag)
    }

    /**
     * 设置日志等级
     * @param tag 标签
     * @param level 日志等级
     */
    fun setLogLevel(tag: String, level: LogLevel) {
        loggers[tag]?.miniLevel = level
    }

    /**
     * 获取日志等级
     * @param tag 标签
     * @return LogLevel
     */
    fun getLogLevel(tag: String): LogLevel? {
        return loggers[tag]?.miniLevel
    }

    /**
     * 获取所有Logger
     * @return Map<String, Logger>
     */
    fun getAll(): Map<String, Logger> {
        return loggers
    }

    /**
     * 设置全局日志等级
     * @param level 日志等级
     */
    fun setGlobalLogLevel(level: LogLevel) {
        loggers.values.forEach {
            it.miniLevel = level
        }
    }
}