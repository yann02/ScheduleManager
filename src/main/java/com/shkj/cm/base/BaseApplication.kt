package com.shkj.cm.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.shkj.cm.common.callback.EmptyCallBack
import com.shkj.cm.common.callback.ErrorCallBack
import com.shkj.cm.common.callback.LoadingCallBack
import com.shkj.cm.common.util.SPreference

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/1/21 10:24
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
open class BaseApplication : Application(), ViewModelStoreOwner {
    lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SPreference.setContext(this)
        initLoadSir()
        mAppViewModelStore = ViewModelStore()
        initLoggerByRetrofit()
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallBack())
            .addCallback(LoadingCallBack())
            .addCallback(EmptyCallBack())
            .commit()
    }

    private fun initLoggerByRetrofit() {
        val strategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 是否显示线程信息，默认为ture
            .methodCount(1)         // 显示的方法行数
            .methodOffset(0)        // 隐藏内部方法调用到偏移量
            .tag("wy_retrofit")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }
}