package com.shkj.cm

import android.app.AlertDialog
import android.content.IntentFilter
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.dosmono.platecommon.util.UIUtils
import com.orhanobut.logger.Logger
import com.shkj.cm.base.view.BaseLifeCycleActivity
import com.shkj.cm.broadcast.VRCalendarContractBroadcast
import com.shkj.cm.databinding.ActivitySmmainBinding
import com.xuexiang.xui.XUI

class SMMainActivity : BaseLifeCycleActivity<MainViewModel, ActivitySmmainBinding>() {

    override fun getLayoutId() = R.layout.activity_smmain

    override fun initView() {
        super.initView()
        XUI.initTheme(this)
        // 隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        initCalenderFilter()
    }

    private fun initCalenderFilter(){

        IntentFilter(CalendarContract.ACTION_EVENT_REMINDER).apply {
            addDataScheme("content")
            registerReceiver(VRCalendarContractBroadcast(),this)
        }
    }

    private fun setDecorViewVisible(isVisible: Boolean) {
        if (isVisible) {
            window.decorView.visibility = View.VISIBLE
        } else {
            window.decorView.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        setDecorViewVisible(false)
    }

    override fun onResume() {
        super.onResume()
        setDecorViewVisible(true)
    }
}