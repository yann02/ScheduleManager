package com.shkj.cm.modules.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dosmono.platecommon.util.PreferencesUtil
import com.dosmono.platecommon.util.UIUtils
import com.dosmono.settings.activity.rouyu.account.LoginActivity
import com.dosmono.settings.utils.AccountUtils
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.common.util.CommonUtil
import com.shkj.cm.databinding.FragmentSmmainBinding
import com.xuexiang.xutil.data.DateUtils

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程备忘首页
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/18 23:23
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
class SMMainFragment : BaseLifeCycleFragment<MainViewModel, FragmentSmmainBinding>(), CalendarView.OnCalendarSelectListener,
    CalendarView.OnMonthChangeListener {
    private val viewModelOfMainActivity by activityViewModels<MainViewModel>()
    private val cDate = CommonUtil.getCurrentDate()
    private val adapterOfSchedule = AdapterOnDaySchedules(mutableListOf())
    override fun getLayoutId() = R.layout.fragment_smmain

    override fun initView() {
        super.initView()
        //隐藏默认的加载框
        showSuccess()
        adapterOfSchedule.setOnItemClickListener { _, _, position ->
            //  用户点击了列表的某一项
            findNavController().navigate(
                R.id.action_smmainFragment_to_detailFragment,
                Bundle().apply {
                    putString(
                        ConstantRouterParamKey.TID,
                        viewModelOfMainActivity.mSchedules.value?.get(position)?.scheTid
                    )
                })
        }
        mDataBinding.rvList.adapter = adapterOfSchedule
        mDataBinding.btnGotoToday.setOnClickListener {
            //  跳转到今天
            mDataBinding.cvCanvasCalendar.scrollToCurrent()
            //  设置选择的日期为今天的日期
            setSelectorDateToTheToday()
        }
        mDataBinding.ibLastYear.setOnClickListener {
            //  上一年
            viewModelOfMainActivity.titleOfYear.value?.let {
                mDataBinding.cvCanvasCalendar.monthViewPager.currentItem =
                    mDataBinding.cvCanvasCalendar.monthViewPager.currentItem - 12
            }
        }
        mDataBinding.ibNextYear.setOnClickListener {
            //  下一年
            viewModelOfMainActivity.titleOfYear.value?.let {
                mDataBinding.cvCanvasCalendar.monthViewPager.currentItem =
                    mDataBinding.cvCanvasCalendar.monthViewPager.currentItem + 12
            }
        }
        mDataBinding.ibLastMonth.setOnClickListener {
            //  上一月
            mDataBinding.cvCanvasCalendar.scrollToPre()
        }
        mDataBinding.ibNextMonth.setOnClickListener {
            //  下一月
            mDataBinding.cvCanvasCalendar.scrollToNext()
        }
        mDataBinding.ibOnAdd.setOnClickListener {
            //  跳转到新建日程页面
            if (viewModelOfMainActivity.monoId.value.isNullOrEmpty()) {
                onDialogForDeleteAllSchedule(
                    requireContext().getString(R.string.not_login),
                    requireContext().getString(R.string.goto_login)
                )
            } else {
                try {
                    findNavController().navigate(R.id.action_smmainFragment_to_formFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        mDataBinding.ibOnList.setOnClickListener {
            //  跳转到日程列表
            try {
                findNavController().navigate(R.id.action_smmainFragment_to_listFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mDataBinding.ibOnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        mDataBinding.cvCanvasCalendar.setOnCalendarSelectListener(this)
        mDataBinding.cvCanvasCalendar.setOnMonthChangeListener(this)
        mDataBinding.cvCanvasCalendar.scrollToCurrent()
    }

    /**
     * 设置当前选择的日期为今天
     */
    private fun setSelectorDateToTheToday() {
        viewModelOfMainActivity.selectorYear.postValue(viewModelOfMainActivity.currentYear.value)
        viewModelOfMainActivity.selectorMonth.postValue(viewModelOfMainActivity.currentMonth.value)
        viewModelOfMainActivity.selectorDay.postValue(viewModelOfMainActivity.currentDay.value)
    }

    override fun initData() {
        super.initData()
        if (viewModelOfMainActivity.currentYear.value != null) {
            return
        }
        //  当前日期
        viewModelOfMainActivity.currentYear.postValue(cDate[0])
        viewModelOfMainActivity.currentMonth.postValue(cDate[1])
        viewModelOfMainActivity.currentDay.postValue(cDate[2])

        //  日历标题日期
        viewModelOfMainActivity.titleOfYear.postValue(cDate[0])
        viewModelOfMainActivity.titleOfMonth.postValue(cDate[1])
        if (voiceQuery()) return
        //  当前选择日期
        viewModelOfMainActivity.selectorYear.postValue(cDate[0])
        viewModelOfMainActivity.selectorMonth.postValue(cDate[1])
        viewModelOfMainActivity.selectorDay.postValue(cDate[2])


    }

    /**
     * 初始化监听
     */
    override fun initDataObserver() {
        super.initDataObserver()
        mDataBinding.vm = viewModelOfMainActivity
        viewModelOfMainActivity.titleOfYear.observe(this, Observer {
            //  加载当月日程标记
            viewModelOfMainActivity.remoteDaysByTheMonth()
        })
        viewModelOfMainActivity.titleOfMonth.observe(this, Observer {
            //  加载当月日程标记
            viewModelOfMainActivity.remoteDaysByTheMonth()
        })
        viewModelOfMainActivity.selectorDay.observe(this, Observer {
            //  加载当天日程
            viewModelOfMainActivity.remoteSchedulesOnDay()
        })
        viewModelOfMainActivity.mSchedules.observe(this, Observer {
            //  监听当天日程数据变化，刷新列表
            if (it != null) {
                adapterOfSchedule.setNewData(it)
//                adapterOfSchedule.setList(it)
                showEmptyList(it.isEmpty())
            }
        })
        //  监听当月日程标记变化
        viewModelOfMainActivity.underPointsForMonthDay.observe(this, Observer {
            mDataBinding.cvCanvasCalendar.setSchemeDate(it)
        })
        //  监听用户ID的变化（存在用户未登录的情况）
        viewModelOfMainActivity.monoId.observe(this, Observer {
            if (it.isNotEmpty()){
                //  加载当天日程
                viewModelOfMainActivity.remoteSchedulesOnDay()
                //  加载当月日程标记
                viewModelOfMainActivity.remoteDaysByTheMonth()
            }
        })
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

    override fun onCalendarOutOfRange(calendar: Calendar?) {}

    /**
     * 监听日历的回调
     * 滑动和点击日历的事件
     */
    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar?.let {
            if (isClick) {
                //  设置当前选择的日期
                if (viewModelOfMainActivity.selectorYear.value != it.year) {
                    //  设置选择的年份
                    viewModelOfMainActivity.selectorYear.postValue(it.year)
                }
                if (viewModelOfMainActivity.selectorMonth.value != it.month) {
                    //  设置选择的月份
                    viewModelOfMainActivity.selectorMonth.postValue(it.month)
                }
                if (viewModelOfMainActivity.selectorDay.value != it.day) {
                    //  设置选择的天
                    viewModelOfMainActivity.selectorDay.postValue(it.day)
                }
            }
        }
    }

    override fun onMonthChange(year: Int, month: Int) {
        //  设置日历标题年月
        if (viewModelOfMainActivity.titleOfYear.value != year) {
            viewModelOfMainActivity.titleOfYear.postValue(year)
        }
        if (viewModelOfMainActivity.titleOfMonth.value != month) {
            viewModelOfMainActivity.titleOfMonth.postValue(month)
        }
    }

    private fun voiceQuery(): Boolean {
        arguments?.getString("handle")?.apply {
            var startTime = arguments?.getString("startTime")
            var date = DateUtils.string2Date(startTime, DateUtils.yyyyMMdd.get())
            var calendar = java.util.Calendar.getInstance()
            calendar.time = date
            viewModelOfMainActivity.selectorYear.postValue(calendar.get(java.util.Calendar.YEAR))
            viewModelOfMainActivity.selectorMonth.postValue(calendar.get(java.util.Calendar.MONTH) + 1)
            viewModelOfMainActivity.selectorDay.postValue(calendar.get(java.util.Calendar.DAY_OF_MONTH))
            try {
                mDataBinding.cvCanvasCalendar.scrollToCalendar(
                    viewModelOfMainActivity.selectorYear.value!!,
                    viewModelOfMainActivity.selectorMonth.value!!,
                    viewModelOfMainActivity.selectorDay.value!!
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }
        return false
    }

    private fun onDialogForDeleteAllSchedule(msg: String, positiveString: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(UIUtils.getContext())

        val alertDialog: AlertDialog = builder.create()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        } else {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
        val view: View = alertDialog.layoutInflater.inflate(R.layout.dialog_schedule, null)
        val tvMessage: TextView = view.findViewById(R.id.tv_alert_message)
        val tvPositive: TextView = view.findViewById(R.id.tv_positive)
        tvMessage.text = msg
        tvPositive.text = positiveString
        builder.setView(view)
        alertDialog.setView(view, 0, 0, 0, 0)
        val wlp: WindowManager.LayoutParams = alertDialog.window?.attributes!!
        wlp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        wlp.y = 1143
        alertDialog.show()
        alertDialog.window?.setLayout(780, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.findViewById<TextView>(R.id.tv_positive).setOnClickListener {
            Intent(requireContext(), LoginActivity::class.java).apply {
                putExtra(LoginActivity.EXTRA_HAS_LOGIN_INFO, AccountUtils.isLogin(requireContext()))
                startActivityForResult(this, 123)
            }
            alertDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.tv_negative).setOnClickListener {
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("wyy", "onActivityResult")
        viewModelOfMainActivity.monoId.postValue(PreferencesUtil.getMonoid(UIUtils.getContext())?.monoid)
    }
}
