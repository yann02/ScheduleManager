package com.shkj.cm.common.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.reflect.ParameterizedType
import java.util.*

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:25
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
object CommonUtil {
    fun showToast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    fun <T> getClass(t: Any): Class<T> {
        // 通过反射 获取父类泛型 (T) 对应 Class类
        return (t.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0]
                as Class<T>
    }

    fun getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun getNightString(skycon : String) : Boolean {
        return skycon.contains("night", ignoreCase = true)
    }

    /**
     * 计算当前日期
     * @return
     */
    fun getCurrentDate(): IntArray {
        val calendar = Calendar.getInstance()
        return intArrayOf(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, calendar[Calendar.DAY_OF_MONTH])
    }

    private var lastClickTime: Long = 0

    /**
     * 防止按钮的连击事件
     * @return true：捕获连击； false：非连击
     */
    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD in 1..999) {
            return true
        }
        lastClickTime = time
        return false
    }
}