package com.shkj.cm.common.util

import android.util.Log
import com.xuexiang.xutil.data.DateUtils
import java.text.DateFormat
import java.util.*

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 时间工具类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/22 9:18
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
object TimeUtil {

    /**
     * 获取默认的开始时间的Calendar（非全天）
     */
    fun getCalendar0fStartTimeOfNotFullDayOnDefault(): Calendar {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        val second = 0
        minute = if (minute < 30) {
            30
        } else {
            hour++
            0
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        return calendar
    }

    /**
     * 获取默认的结束时间的Calendar（非全天）
     */
    fun getCalendar0fEndTimeOfNotFullDayOnDefault(): Calendar {
        val calendar = getCalendar0fStartTimeOfNotFullDayOnDefault()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        calendar.set(Calendar.HOUR_OF_DAY, ++hour)
        return calendar
    }

    /**
     * 获取开始的格式化时间（非全天）
     */
    fun getFormatDateStringForStartTimeThatNotFullDay(format: DateFormat): String {
        val calendar = getCalendar0fStartTimeOfNotFullDayOnDefault()
        val date = calendar.time
//        return DateUtils.date2String(date, DateUtils.yyyyMMddHHmm.get())
        return DateUtils.date2String(date, format)
    }

    /**
     * 获取结束的格式化时间（非全天）
     */
    fun getFormatDateStringForEndTimeThatNotFullDay(format: DateFormat): String {
        val calendar = getCalendar0fEndTimeOfNotFullDayOnDefault()
        val date = calendar.time
//        return DateUtils.date2String(date, DateUtils.yyyyMMddHHmm.get())
        return DateUtils.date2String(date, format)
    }

    /**
     * 根据时间戳字符串返回Calendar
     */
    fun getCalendarByMillisecondString(mss: String): Calendar {
        val ms = mss.toLong()
        val calendar = Calendar.getInstance()
        calendar.time = Date(ms)
        return calendar
    }

    /**
     * 根据时间戳字符串返回指定格式的时间字符串
     */
    fun getCalendarByDateFormString(mss: String, formatOfResult: DateFormat): String {
        return DateUtils.date2String(Date(mss), formatOfResult)
    }

    //加九个小时
    fun datePlus9Hours2Millis(date:Date):Long{
        var calendar  = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR,10)
        return DateUtils.date2Millis(calendar.time)
    }

    //加10个小时
    fun datePlus10Hours2Millis(date:Date):Long{
        var calendar  = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR,11)
        return DateUtils.date2Millis(calendar.time)
    }

    /**
     * 全天转非全天
     */
    fun fullOfDayToNotFullOfDay(formatString: String): String {
        val date = DateUtils.string2Date(formatString, DateUtils.yyyyMMdd.get())
        return DateUtils.date2String(date, DateUtils.yyyyMMddHHmm.get())
    }

    /**
     * 非全天转全天
     */
    fun notFullOfDayToFullOfDay(formatString: String): String {
        val date = DateUtils.string2Date(formatString, DateUtils.yyyyMMddHHmm.get())
        return DateUtils.date2String(date, DateUtils.yyyyMMdd.get())
    }

    /**
     * 首次获取非全天的开始时间字符串
     */
    fun getNotFullOfDay(): String {
        val calendar = getCalendar0fStartTimeOfNotFullDayOnDefault()
        val date = calendar.time
        return DateUtils.date2String(date, DateUtils.yyyyMMddHHmm.get())
    }

    /**
     * 首次获取非全天的结束时间字符串
     */
    fun getNotFullOfDayForEndTime(): String {
        val calendar = getCalendar0fEndTimeOfNotFullDayOnDefault()
        val date = calendar.time
        return DateUtils.date2String(date, DateUtils.yyyyMMddHHmm.get())
    }

    /**
     * 首次获取全天的开始时间字符串
     */
    fun getFullOfDay(): String {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        return DateUtils.date2String(date, DateUtils.yyyyMMdd.get())
    }

    /**
     * 首次获取全天的结束时间字符串
     */
    fun getFullOfDayForEndTime(): String {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        return DateUtils.date2String(date, DateUtils.yyyyMMdd.get())
    }

    /**
     * 根据年月日获取毫秒数（用于获取当天日程列表的接口请求参数）
     */
    fun getMilliStringByYearMonthDay(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, 0, 0, 0)
        val date = calendar.time
        val formDateString = DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get())
        Log.d("wyy", "formDateString:$formDateString")
        return DateUtils.string2Millis(formDateString, DateUtils.yyyyMMddHHmmss.get()).toString()
    }

    fun getEndTimeByStartTime(startTime:String):String{
        val date = DateUtils.string2Date(startTime, DateUtils.yyyyMMddHHmm.get())
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR,1)
        return DateUtils.date2String(calendar.time,DateUtils.yyyyMMddHHmm.get())
    }
}