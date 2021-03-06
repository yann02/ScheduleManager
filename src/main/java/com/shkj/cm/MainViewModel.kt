package com.shkj.cm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dosmono.platecommon.util.PreferencesUtil
import com.dosmono.platecommon.util.UIUtils
import com.haibin.calendarview.Calendar
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.initiateRequestNotState
import com.shkj.cm.common.util.TimeUtil
import com.shkj.cm.modules.main.entities.result.Body
import com.shkj.cm.modules.main.entities.result.BodyOnDaysByTheMonthEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程备忘首页
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/16 22:57
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */

class MainViewModel : BaseViewModel<MainRepository>() {

    /**
     * 用户id
     */
    val monoId = MutableLiveData<String>()

    init {
        monoId.postValue(PreferencesUtil.getMonoid(UIUtils.getContext())?.monoid)
    }

    //  日历的年月
    var titleOfYear = MutableLiveData<Int>()
    var titleOfMonth = MutableLiveData<Int>()

    //  当前选择的年月日
    var selectorYear = MutableLiveData<Int>()
    var selectorMonth = MutableLiveData<Int>()
    var selectorDay = MutableLiveData<Int>()

    //  当天的日期
    var currentYear = MutableLiveData<Int>()
    var currentMonth = MutableLiveData<Int>()
    var currentDay = MutableLiveData<Int>()

    //  首页界面右侧日程列表数据
    var mSchedules = MutableLiveData<List<Body>>()

    //  某月内，用户所有日程管理的日期集合（后台返回的数据）
    var mBodyOnDaysByTheMonthEntities = MutableLiveData<List<BodyOnDaysByTheMonthEntity>>()

    //  某月内，用户所有日程管理的日期集合(日历中使用)
    val underPointsForMonthDay = mBodyOnDaysByTheMonthEntities.map {
        val year = titleOfYear.value
        val month = titleOfMonth.value
        mutableMapOf<String, Calendar>().apply {
            for (item: BodyOnDaysByTheMonthEntity in it) {
                put(
                    getSchemeCalendar(year, month, item.endTime.toInt()).toString(),
                    getSchemeCalendar(year, month, item.endTime.toInt())
                )
            }
        }
    }

    var deleteOnEdit = MutableLiveData(false)

    private fun getSchemeCalendar(year: Int?, month: Int?, day: Int): Calendar {
        return Calendar().apply {
            year?.let {
                this.year = it
            }
            month?.let {
                this.month = it
            }
            this.day = day
        }
    }

    var jobOfDaysByMonth: Job? = null

    /**
     * 获取用户某日，所有日程
     */
    fun remoteSchedulesOnDay() {
        if (!monoId.value.isNullOrEmpty()) {
            selectorYear.value?.let { year ->
                selectorMonth.value?.let { month ->
                    selectorDay.value?.let {
                        //  sechStartTime 每天的开始时间：例如：2020-04-01 00:00:00的时间戳
                        val sechStartTime = TimeUtil.getMilliStringByYearMonthDay(year, month, it)
                        initiateRequestNotState {
                            val httpBaseBean = mRepository.remoteSchedulesOnDay(monoId.value!!, sechStartTime = sechStartTime)
                            if (httpBaseBean.code == 8000) {
                                mSchedules.postValue(httpBaseBean.body)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取某月内，用户所有日程管理的日期集合
     */
    fun remoteDaysByTheMonth() {
        if (!monoId.value.isNullOrEmpty()) {
            titleOfYear.value?.let { year ->
                titleOfMonth.value?.let { month ->
                    //  sechStartTime 每天的开始时间：例如：2020-04-01 00:00:00的时间戳
                    val sechStartTime = TimeUtil.getMilliStringByYearMonthDay(year, month, 1)
                    jobOfDaysByMonth?.cancel()
                    jobOfDaysByMonth = viewModelScope.launch {
                        val httpBaseBean = mRepository.remoteDaysByTheMonth(monoId.value!!, sechStartTime = sechStartTime)
                        if (httpBaseBean.code == 8000) {
                            mBodyOnDaysByTheMonthEntities.postValue(httpBaseBean.body)
                        }
                    }
                }
            }
        }
    }
}