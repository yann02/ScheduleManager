package com.shkj.cm.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import com.shkj.cm.R

class ScheduleMonthView(context: Context) : MonthView(context) {
    /**
     * 自定义魅族标记的文本画笔
     */
    private val mTextPaint = Paint()

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

    private var mPadding = 0

    private var mCircleRadius = 0f

    /**
     * 自定义魅族标记的圆形背景
     */
    private val mSchemeBasicPaint = Paint()

    private var mSchemeBaseLine = 0f

    init {
        mTextPaint.textSize = dipToPx(context, 8f).toFloat()
        mTextPaint.color = -0x1
        mTextPaint.isAntiAlias = true
        mTextPaint.isFakeBoldText = true

        mSchemeBasicPaint.isAntiAlias = true
        mSchemeBasicPaint.style = Paint.Style.FILL
        mSchemeBasicPaint.textAlign = Paint.Align.CENTER
        mSchemeBasicPaint.isFakeBoldText = true
        mSchemeBasicPaint.color = Color.WHITE


        mCurrentDayPaint.isAntiAlias = true
        mCurrentDayPaint.style = Paint.Style.FILL
        mCurrentDayPaint.color = ContextCompat.getColor(context, R.color.selector_color_calendar)

        mPointPaint.isAntiAlias = true
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.textAlign = Paint.Align.CENTER
        mPointPaint.color = Color.RED

        mCircleRadius = dipToPx(getContext(), 7f).toFloat()

        val metrics = mSchemeBasicPaint.fontMetrics
        mSchemeBaseLine =
            mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1f)
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onDrawSelected(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int, hasScheme: Boolean): Boolean {
        if (!calendar!!.isCurrentDay) {
            mSelectedPaint.style = Paint.Style.STROKE
            mSelectedPaint.strokeWidth = 2f
            val rectF = RectF(x.toFloat(), y.toFloat(), (x + mItemWidth).toFloat(), (y + mItemHeight).toFloat())
            canvas!!.drawRoundRect(rectF, 16f, 16f, mSelectedPaint)
        }
        return true
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int) {
        mPointPaint.color = ContextCompat.getColor(context, R.color.dot_color)
        canvas!!.drawCircle((x + mItemWidth / 2).toFloat(), (y + mItemHeight - 13).toFloat(), mPointRadius, mPointPaint)
    }

    override fun onDrawText(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        val cx = x + mItemWidth / 2
        if (calendar!!.isCurrentDay) {
            val rectF = RectF(x.toFloat(), y.toFloat(), (x + mItemWidth).toFloat(), (y + mItemHeight).toFloat())
            canvas!!.drawRoundRect(rectF, 16f, 16f, mCurrentDayPaint)
        }
        val drawPaint = if (isSelected && !calendar.isCurrentDay) {
            mSelectTextPaint
        } else if (hasScheme) {
            if (calendar.isCurrentMonth) mSchemeTextPaint else mOtherMonthTextPaint
        } else {
            if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint
        }
        canvas!!.drawText(calendar.day.toString(), cx.toFloat(), mTextBaseLine + y, drawPaint)
    }
}