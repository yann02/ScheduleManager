package com.shkj.cm.base.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.util.CommonUtil

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
abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding>() : Fragment() {
    protected lateinit var mViewModel: VM

    protected lateinit var mDataBinding: DB

    protected lateinit var loadService: LoadService<*>

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() = initData()

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mDataBinding.lifecycleOwner = this
        loadService = LoadSir.getDefault().register(mDataBinding.root) { reLoad() }
        return loadService.loadLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))
        initView()
        initData()
    }
}