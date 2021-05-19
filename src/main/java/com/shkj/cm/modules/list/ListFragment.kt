package com.shkj.cm.modules.list

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.navController
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.databinding.FragmentListBinding
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
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
    override fun getLayoutId() = R.layout.fragment_list

    override fun initView() {
        super.initView()
        showSuccess()
        //  初始化用户ID
        mViewModel.monoId = viewModelOfMainActivity.monoId.value
        initAdapter()
        mDataBinding.ibOnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        mDataBinding.btnCleanUp.setOnClickListener {
            adapter?.let {
                if (it.itemCount > 0) {
                    //  数据不为空时再提示
                    onDialogForDeleteAllSchedule(getString(R.string.tip_of_delete_all_schedule), true)
                }
            }
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
        if (!mViewModel.monoId.isNullOrEmpty()) {
            initPagingData()
        }
    }

    private fun initPagingData() {
        lifecycleScope.launch {
            mViewModel.findSchedulesByDB().collectLatest {
                adapter?.submitData(it)
                voiceDelete()
            }
        }
    }

    private fun initAdapter() {
        adapter = AdapterOnPageSchedules(object : PageScheduleViewHolder.OnPageListItemClickListener {
            override fun onItemClick(position: Int, tid: String) {
                //  点击了日程列表的某一项
                navController(
                    this@ListFragment,
                    R.id.action_listFragment_to_detailFragment,
                    Bundle().apply { putString(ConstantRouterParamKey.TID, tid) })
//                findNavController().navigate(
//                    R.id.action_listFragment_to_detailFragment,
//                    Bundle().apply { putString(ConstantRouterParamKey.TID, tid) })
            }

            override fun onItemDeleteClick(position: Int, tid: String) {
                //  点击了列表的删除按钮
                onDialogForDeleteAllSchedule(getString(R.string.tip_of_delete_schedule), false, tid)
            }
        })
        adapter!!.addLoadStateListener {
            // show empty list
            val isListEmpty = it.refresh is LoadState.NotLoading && adapter!!.itemCount == 0
            showEmptyList(isListEmpty)
        }
        mDataBinding.rvList.adapter = adapter
    }

    private fun voiceDelete() {
        arguments?.getString("handle")?.apply {
            MaterialDialog.Builder(requireContext())
                .content(R.string.tip_of_delete_all_schedule)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive { _, _ -> mViewModel.deleteAllOfSchedules() }
                .show()
        }
    }

    fun onDialogForDeleteAllSchedule(msg: String, deleteAll: Boolean, tid: String = "") {
        val builder: AlertDialog.Builder = AlertDialog.Builder(UIUtils.getContext())

        val alertDialog: AlertDialog = builder.create()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        } else {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
        val view: View = alertDialog.layoutInflater.inflate(R.layout.dialog_schedule, null)
        val tvMessage: TextView = view.findViewById(R.id.tv_alert_message)
        tvMessage.text = msg
        builder.setView(view)
        alertDialog.setView(view, 0, 0, 0, 0)
        val wlp: WindowManager.LayoutParams = alertDialog.window?.attributes!!
        wlp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        wlp.y = 1143
        alertDialog.show()
        alertDialog.window?.setLayout(780, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.findViewById<TextView>(R.id.tv_positive).setOnClickListener {
            if (deleteAll) {
                mViewModel.deleteAllOfSchedules()
            } else {
                if (tid.isNotEmpty()) {
                    mViewModel.deleteSchedule(tid)
                }
            }
            alertDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.tv_negative).setOnClickListener {
            alertDialog.dismiss()
        }
    }
}