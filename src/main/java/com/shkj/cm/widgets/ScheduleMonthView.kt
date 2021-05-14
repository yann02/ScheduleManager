package com.shkj.cm.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.core.content.ContextCompat
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import com.shkj.cm.R

class ScheduleMonthView(context: Context) : MonthView(context) {
    /**
     * 背景圆点
     */
    private val mPointPaint = Paint()

    /**
     * 今天的背景色
     */
    private val mCurrentDayPaint = Paint()

    /**
     * 圆点半径
     */
    private var mPointRadius = 3f

    init {
        mCurrentDayPaint.isAntiAlias = true
        mCurrentDayPaint.style = Paint.Style.FILL
        mCurrentDayPaint.color = ContextCompat.getColor(context, R.color.selector_color_calendar)
        mPointPaint.isAntiAlias = true
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.textAlign = Paint.Align.CENTER
        mPointPaint.color = ContextCompat.getColor(context, R.color.dot_color)
    }

    /**
     * 绘制选中的日子（这个绘制会覆盖标记点的显示）
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    override fun onDrawSelected(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean): Boolean {
        if (!calendar.isCurrentDay) {
            mSelectedPaint.style = Paint.Style.STROKE
            mSelectedPaint.strokeWidth = 2f
            val rectF = RectF(x.toFloat(), y.toFloat(), (x + mItemWidth).toFloat(), (y + mItemHeight).toFloat())
            canvas.drawRoundRect(rectF, 16f, 16f, mSelectedPaint)
        }
        return true
    }

    /**
     * 绘制标记的事件日子(只绘制非当天的标记点，避免重复绘制)
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int) {
        if (!calendar.isCurrentDay) {
            canvas.drawCircle((x + mItemWidth / 2).toFloat(), (y + mItemHeight - 13).toFloat(), mPointRadius, mPointPaint)
        }
    }

    /**
     * 绘制文本（如果今天有标记点，这里还需要绘制当天的标记点）
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    override fun onDrawText(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        val cx = x + mItemWidth / 2
        calendar?.let {
            canvas?.let { cit ->
                if (it.isCurrentDay) {
                    val rectF = RectF(x.toFloat(), y.toFloat(), (x + mItemWidth).toFloat(), (y + mItemHeight).toFloat())
                    cit.drawRoundRect(rectF, 16f, 16f, mCurrentDayPaint)
                    if (hasScheme) {
                        //  绘制当天的标记点
                        cit.drawCircle(
                            (x + mItemWidth / 2).toFloat(),
                            (y + mItemHeight - 13).toFloat(),
                            mPointRadius,
                            mPointPaint
                        )
                    }
                }
                val drawPaint = if (isSelected && !it.isCurrentDay) {
                    mSelectTextPaint
                } else if (hasScheme) {
                    if (it.isCurrentMonth) mSchemeTextPaint else mOtherMonthTextPaint
                } else {
                    if (it.isCurrentDay) mCurDayTextPaint else if (it.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint
                }
                cit.drawText(it.day.toString(), cx.toFloat(), mTextBaseLine + y, drawPaint)
            }
        }
    }
}