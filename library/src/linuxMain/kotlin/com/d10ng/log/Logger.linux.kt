package com.d10ng.log

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.posix.localtime_r
import platform.posix.strftime
import platform.posix.time_tVar
import platform.posix.tm


@OptIn(ExperimentalForeignApi::class)
fun formatTimestamp(tsMillis: Long): String = memScoped {
    val seconds = tsMillis / 1000
    val milli = (tsMillis % 1000).toInt()
    val milliStr = if (milli < 10) "00$milli" else if (milli < 100) "0$milli" else "$milli"

    val t = alloc<time_tVar>()
    t.value = seconds

    // 使用 localtime_r（线程安全）
    val tmStruct = alloc<tm>()
    localtime_r(t.ptr, tmStruct.ptr)?: return "$seconds.$milliStr"

    val buffer = allocArray<ByteVar>(64)
    strftime(buffer, 64.convert(), "%F %T", tmStruct.ptr)

    val base = buffer.toKString()
    "$base.$milliStr"
}

actual fun platformLog(data: LogEntry) {
    if (data.level == LogLevel.NONE) return
    println("[${formatTimestamp(data.time)}] $data")
}