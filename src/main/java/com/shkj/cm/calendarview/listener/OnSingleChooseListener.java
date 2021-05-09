package com.shkj.cm.calendarview.listener;

import android.view.View;

import com.shkj.cm.calendarview.bean.DateBean;

/**
 * 日期点击接口
 */
public interface OnSingleChooseListener {
    /**
     * @param view
     * @param date
     */
    void onSingleChoose(View view, DateBean date);
}
