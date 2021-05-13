package com.shkj.cm.modules.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.calendarview.utils.CalendarUtil
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.databinding.FragmentSmmainBinding
import kotlin.math.log

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
class SMMainFragment : BaseLifeCycleFragment<MainViewModel, FragmentSmmainBinding>() {
    private val viewModelOfMainActivity by activityViewModels<MainViewModel>()
    private val cDate = CalendarUtil.getCurrentDate()
    private val adapterOfSchedule = AdapterOnDaySchedules(mutableListOf())
    override fun getLayoutId() = R.layout.fragment_smmain

    override fun initView() {
        super.initView()
        //隐藏默认的加载框
        showSuccess()
        //  初始化当前日期
        initDate()
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
        mDataBinding.cvCalendar.setOnPagerChangeListener {
            //  监听日历切换
            viewModelOfMainActivity.titleOfYear.postValue(it[0])
            viewModelOfMainActivity.titleOfMonth.postValue(it[1])
        }
        mDataBinding.cvCalendar.setOnSingleChooseListener { _, date ->
            //  监听用户点击日历的某一项
            viewModelOfMainActivity.selectorYear.postValue(date.solar[0])
            viewModelOfMainActivity.selectorMonth.postValue(date.solar[1])
            viewModelOfMainActivity.selectorDay.postValue(date.solar[2])
        }
        mDataBinding.btnGotoToday.setOnClickListener {
            //  跳转到今天
            if (!selectorDateNotToday()) {
                mDataBinding.cvCalendar.today()
                //  设置选择的日期为今天的日期
                setSelectorDateToTheToday()
            }
        }
        mDataBinding.ibLastYear.setOnClickListener {
            //  上一年
            mDataBinding.cvCalendar.lastYear()
        }
        mDataBinding.ibNextYear.setOnClickListener {
            //  下一年
            mDataBinding.cvCalendar.nextYear()
        }
        mDataBinding.ibLastMonth.setOnClickListener {
            //  上一月
            mDataBinding.cvCalendar.lastMonth()
        }
        mDataBinding.ibNextMonth.setOnClickListener {
            //  下一月
            mDataBinding.cvCalendar.nextMonth()
        }
        mDataBinding.ibOnAdd.setOnClickListener {
            //  跳转到新建日程页面
            findNavController().navigate(R.id.action_smmainFragment_to_formFragment)
        }
        mDataBinding.ibOnList.setOnClickListener {
            //  跳转到日程列表
            findNavController().navigate(R.id.action_smmainFragment_to_listFragment)
        }
        mDataBinding.ibOnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        //  初始化日历
        initCalendar()
    }

    /**
     * 初始化日历
     */
    private fun initCalendar() {
        var singDate = ""
        if (viewModelOfMainActivity.selectorDay.value != null) {
            singDate =
                "${viewModelOfMainActivity.selectorYear.value}.${viewModelOfMainActivity.selectorMonth.value}.${viewModelOfMainActivity.selectorDay.value}"
        }
        mDataBinding.cvCalendar
            .setInitDate("${cDate[0]}.${cDate[1]}")
            .setSingleDate(singDate)
            .setCurrentDate("${cDate[0]}.${cDate[1]}.${cDate[2]}")
            .init()
    }

    /**
     * 设置当前选择的日期为今天
     */
    private fun setSelectorDateToTheToday() {
        viewModelOfMainActivity.selectorYear.postValue(viewModelOfMainActivity.currentYear.value)
        viewModelOfMainActivity.selectorMonth.postValue(viewModelOfMainActivity.currentMonth.value)
        viewModelOfMainActivity.selectorDay.postValue(viewModelOfMainActivity.currentDay.value)
    }

    /**
     * 判断选择的日期是否为当天的日期
     * @return true : 是当天 ；false ： 不是当天
     */
    private fun selectorDateNotToday(): Boolean {
        if (viewModelOfMainActivity.titleOfYear.value != viewModelOfMainActivity.currentYear.value) {
            return false
        }
        if (viewModelOfMainActivity.titleOfMonth.value != viewModelOfMainActivity.currentMonth.value) {
            return false
        }
        if (viewModelOfMainActivity.selectorDay.value != viewModelOfMainActivity.currentDay.value) {
            return false
        }
        if (viewModelOfMainActivity.selectorMonth.value != viewModelOfMainActivity.currentMonth.value) {
            return false
        }
        if (viewModelOfMainActivity.selectorYear.value != viewModelOfMainActivity.currentYear.value) {
            return false
        }
        return true
    }

    /**
     * 初始化当前日期
     */
    private fun initDate() {
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
            viewModelOfMainActivity.remoteDaysByTheMonth()
        })
        viewModelOfMainActivity.titleOfMonth.observe(this, Observer {
            viewModelOfMainActivity.remoteDaysByTheMonth()
        })
        viewModelOfMainActivity.selectorDay.observe(this, Observer {
            Log.d("wyy", "当天$it")
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
        viewModelOfMainActivity.mIntBodyOnDaysByTheMonthEntities.observe(this, Observer {
            try {
                mDataBinding.cvCalendar.refreshDot(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        //  监听当月日程标记变化
        viewModelOfMainActivity.underPointsForMonthDay.observe(this, Observer {
            mDataBinding.cvCanvasCalendar.setSchemeDate(it)
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

}