package com.shkj.cm.widgets

import android.content.Context
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.shkj.cm.R
import kotlinx.android.synthetic.main.layout_frequency_view.view.*
import kotlin.properties.Delegates

class FrequencyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var isAdd = false

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.frequency_style)
        isAdd = typeArray.getBoolean(R.styleable.frequency_style_is_add, false)

        initView()
        typeArray.recycle()
    }

    private fun initView() {
        View.inflate(context, R.layout.layout_frequency_view, this)
        ib_delete_frequency.visibility = if (isAdd) View.GONE else View.VISIBLE
        ib_add_frequency.visibility = if (isAdd) View.VISIBLE else View.GONE
    }

    fun resetSelectedIndex() {
        materialspinner.selectedIndex = 0
    }

    fun getSelectIndex():Int{
        return materialspinner.selectedIndex
    }

    fun setSelectedIndex(index: Int) {
        materialspinner.selectedIndex = index
    }
}
