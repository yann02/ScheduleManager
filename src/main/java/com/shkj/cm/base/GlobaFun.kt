package com.shkj.cm.base

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shkj.cm.R
import com.shkj.cm.widgets.FrequencyView
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner


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
var lastClickTime = 0L
fun navController(fragment:Fragment, resId:Int,bundle:Bundle) {
    val MIN_CLICK_DELAY_TIME = 500

    var curClickTime = System.currentTimeMillis()
    if(curClickTime - lastClickTime > MIN_CLICK_DELAY_TIME){
        fragment.findNavController().navigate(resId,bundle)
    }
    lastClickTime = curClickTime;
}

fun navController(fragment:Fragment, resId:Int) {
    val MIN_CLICK_DELAY_TIME = 500

    var curClickTime = System.currentTimeMillis()
    if(curClickTime - lastClickTime > MIN_CLICK_DELAY_TIME){
        fragment.findNavController().navigate(resId)
    }
    lastClickTime = curClickTime;
}