package com.shkj.cm

import android.view.View
import com.shkj.cm.base.view.BaseLifeCycleActivity
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