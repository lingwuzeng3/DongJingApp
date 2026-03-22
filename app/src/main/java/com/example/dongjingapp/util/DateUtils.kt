package com.example.dongjingapp.util

import java.util.Calendar

/** 本周起始时间戳（按设备时区、一周从周日或周一取决于 Calendar 配置） */
fun weekStartMillis(): Long {
    val c = Calendar.getInstance()
    c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek)
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return c.timeInMillis
}
