package com.shkj.cm

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.shkj.cm.base.view.BaseLifeCycleActivity
import com.shkj.cm.common.Constant
import com.shkj.cm.databinding.ActivitySmmainBinding
import com.shkj.cm.widgets.NormalDialogByFragment
import com.xuexiang.xui.XUI

class SMMainActivity : BaseLifeCycleActivity<MainViewModel, ActivitySmmainBinding>() {

    override fun getLayoutId() = R.layout.activity_smmain

    override fun initView() {
        super.initView()
        XUI.initTheme(this)
        // 隐藏状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        dispatch()
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

    fun dispatch() {
        var call = intent.getBooleanExtra("call", false)
        if (!call) {
            return
        }
        var handle = intent.getStringExtra("handle")
        var startTime = intent.getStringExtra("startTime")
        var content = intent.getStringExtra("content")
        dispatchFragment(handle!!, startTime!!, content!!)
    }

    private fun dispatchFragment(handle: String, starTime: String, content: String) {

        findNavController(R.id.fragment).apply {

            setGraph(navInflater.inflate(R.navigation.dialogue_nav).apply {
                startDestination = when (handle) {
                    Constant.VOICE_ACTION_ADD -> R.id.formFragment
                    Constant.VOICE_ACTION_DELETE -> R.id.listFragment
                    Constant.VOICE_ACTION_UPDATE -> R.id.formFragment
                    else -> R.id.smmainFragment
                }
            }, Bundle().apply {
                putString("startTime", starTime)
                putString("content", content)
                putString("handle", handle)
            })
        }
    }
}