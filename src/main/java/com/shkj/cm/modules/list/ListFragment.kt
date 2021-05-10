package com.shkj.cm.modules.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程列表
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/20 17:34
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
class ListFragment : BaseLifeCycleFragment<ListViewModel, FragmentListBinding>() {
    private val viewModelOfMainActivity by activityViewModels<MainViewModel>()

    //  日程列表适配器
    private var adapter: AdapterOnPageSchedules? = null

    //  当前选中的日程删除项下标
    private var deletePosition: Int? = null
    override fun getLayoutId() = R.layout.fragment_list

    override fun initView() {
        super.initView()
        showSuccess()
        //  初始化用户ID
        mViewModel.monoId = viewModelOfMainActivity.monoId
        initAdapter()
        mDataBinding.ibOnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        mDataBinding.btnCleanUp.setOnClickListener {
            mViewModel.deleteAllOfSchedules()
        }
    }

    /**
     * 设置日程列表是否显示为空页面
     */
    private fun showEmptyList(listEmpty: Boolean) {
        if (listEmpty) {
            //  列表为空时隐藏列表和显示为空的视图
            mDataBinding.tvNotSchedule.visibility = View.VISIBLE
            mDataBinding.rvList.visibility = View.GONE
        } else {
            //  列表不为空时显示列表和隐藏为空的视图
            mDataBinding.tvNotSchedule.visibility = View.GONE
            mDataBinding.rvList.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        super.initData()
        initPagingData()
    }

    private fun initPagingData() {
        lifecycleScope.launch {
//            mViewModel.findSchedules().collectLatest {
//                adapter?.submitData(it)
//            }
            mViewModel.findSchedulesByDB().collectLatest {
                adapter?.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        adapter = AdapterOnPageSchedules(object : PageScheduleViewHolder.OnPageListItemClickListener {
            override fun onItemClick(position: Int, tid: String) {
                //  点击了日程列表的某一项
                findNavController().navigate(
                    R.id.action_listFragment_to_detailFragment,
                    Bundle().apply { putString(ConstantRouterParamKey.TID, tid) })
            }

            override fun onItemDeleteClick(position: Int, tid: String) {
                //  点击了列表的删除按钮
                deletePosition = position
                mViewModel.deleteSchedule(tid, position)
            }
        })
        adapter!!.addLoadStateListener {
            // show empty list
            val isListEmpty = it.refresh is LoadState.NotLoading && adapter!!.itemCount == 0
            showEmptyList(isListEmpty)
        }
        mDataBinding.rvList.adapter = adapter
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.deleteTag.observe(this, Observer {
            initPagingData()
        })
        mViewModel.deleteAllOfScheduleTag.observe(this, Observer {
            initPagingData()
        })
    }

    override fun onPause() {
        super.onPause()
        Log.d("wyy","onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("wyy","onDestroy")
    }
}