package com.shkj.cm.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.shkj.cm.calendarview.weiget.WeekView

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 自定义日历的周，设置背景为透明
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/19 14:00
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class MyWeekView(context: Context, attrs: AttributeSet) : WeekView(context, attrs) {
    init {
        setBackgroundColor(Color.TRANSPARENT)
    }
}