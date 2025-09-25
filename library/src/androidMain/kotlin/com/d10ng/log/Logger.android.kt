package com.d10ng.log

import android.util.Log

actual fun platformLog(data: LogEntry) {
    if (data.level == LogLevel.NONE) return
    Log.println(when (data.level) {
        LogLevel.VERBOSE -> Log.VERBOSE
        LogLevel.DEBUG -> Log.DEBUG
        LogLevel.INFO -> Log.INFO
        LogLevel.WARN -> Log.WARN
        LogLevel.ERROR -> Log.ERROR
        else -> Log.VERBOSE
    }, data.tag, data.message)
}