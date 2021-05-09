package com.shkj.cm.base.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dosmono.platecommon.basic.ui.activity.BaseActivity
import com.dosmono.platecommon.ui.mvp.IPresenter
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.AppManager
import com.shkj.cm.common.util.CommonUtil
import org.apache.commons.lang3.ObjectUtils

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:23
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
abstract class BaseActivity<VM : BaseViewModel<*>, DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() {}

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBinding.lifecycleOwner = this
        AppManager.instance.addActivity(this)
        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))
        initView()
        initData()
    }

    val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(this) {
            reLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.removeActivity(this)
    }
}