package com.shkj.cm.common.util

import android.view.View
import android.widget.ImageButton
import com.shkj.cm.R
import com.shkj.cm.widgets.FrequencyView
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner
import com.xuexiang.xutil.data.DateUtils
import java.util.*


fun initFrequencySelectListener(vararg arr: FrequencyView, callback: (FrequencyView, Int) -> Unit) {
    repeat(arr.count()) { index: Int ->
        var materialSpinner = arr[index].findViewById<MaterialSpinner>(R.id.materialspinner)
        materialSpinner.setOnItemSelectedListener { view, position, id, item ->
            callback(arr[index], position)
        }
    }
}

fun FrequencyView.bindingAddFrequencyCallback(callback: (FrequencyView) -> Unit) {
    var addFrequencyView = this.findViewById<ImageButton>(R.id.ib_add_frequency)
    addFrequencyView.setOnClickListener {
        callback(this)
    }
}

fun bindingDeleteFrequencyCallback(vararg arr: FrequencyView, callback: (FrequencyView) -> Unit) {
    for (view in arr) {
        var deleteFrequencyView = view.findViewById<ImageButton>(R.id.ib_delete_frequency)
        deleteFrequencyView.setOnClickListener {
            view.visibility = View.GONE
            callback(view)
        }
    }
}

fun getWeekOfMonth(date: Date):Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MONTH)
}