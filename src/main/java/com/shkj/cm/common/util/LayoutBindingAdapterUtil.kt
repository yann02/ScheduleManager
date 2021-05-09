package com.shkj.cm.common.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.shkj.cm.R
import com.xuexiang.xutil.data.DateUtils
import java.util.*

object LayoutBindingAdapterUtil {
    /**
     * 加载图片
     */
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view).load(it).into(view)
        }
    }

    /**
     * 首页日程列表
     * 左侧时间设置
     */
    @BindingAdapter("app:bindOnTimeForDayScheduleItem")
    @JvmStatic
    fun bindOnTimeForDayScheduleItem(view: TextView, time: String?) {
        time?.let {
            val date = DateUtils.millis2Date(time.toLong())
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            var preName = view.context.getString(R.string.am)
            if (hour >= 13) {
                preName = view.context.getString(R.string.pm)
            }
            val sb = StringBuilder(preName)
            val name = DateUtils.date2String(date, DateUtils.HHmm.get())
            sb.append(name)
            view.text = sb
        }
    }

    /**
     * 首页日程列表
     * 左侧线条颜色设置
     */
    @BindingAdapter("app:bindOnLineColorForDayScheduleItem")
    @JvmStatic
    fun bindOnLineColorForDayScheduleItem(view: TextView, position: Int?) {
        position?.let {
            val pos = it + 1
            val color = when (pos % 4) {
                1 -> view.context.getColor(R.color.line_color_on_first)
                2 -> view.context.getColor(R.color.line_color_on_second)
                3 -> view.context.getColor(R.color.line_color_on_third)
                else -> view.context.getColor(R.color.line_color_on_fourth)
            }
            view.setBackgroundColor(color)
        }
    }
}