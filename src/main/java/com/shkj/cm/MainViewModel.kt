package com.shkj.cm

import androidx.lifecycle.LiveData
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
//  测试用默认用户ID
const val MONOID_DEFAULT = "2000206"

class MainViewModel : BaseViewModel<MainRepository>() {

    /**
     * 用户id
     */
    val monoId: String by lazy {
        val user = PreferencesUtil.getMonoid(UIUtils.getContext())
        val id = user?.monoid
        //  不存在时则用默认id
        if (id.isNullOrEmpty()) MONOID_DEFAULT else id
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

    val underPointsForMonthDay = mBodyOnDaysByTheMonthEntities.map {
        val year = selectorYear.value
        val month = selectorMonth.value
        mutableMapOf<String, Calendar>().apply {
            for (item: BodyOnDaysByTheMonthEntity in it) {
                put(
                    getSchemeCalendar(year, month, item.endTime.toInt()).toString(),
                    getSchemeCalendar(year, month, item.endTime.toInt())
                )
            }
        }
    }

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

    //  某月内，用户所有日程管理的日期集合(日历中使用)
    var mIntBodyOnDaysByTheMonthEntities: LiveData<List<Int>> = mBodyOnDaysByTheMonthEntities.map {
        val res = mutableListOf<Int>()
        for (item: BodyOnDaysByTheMonthEntity in it) {
            res.add(item.endTime.toInt())
        }
        res.sort()
        res
    }

    /**
     * 获取用户某日，所有日程
     */
    fun remoteSchedulesOnDay() {
        selectorYear.value?.let { year ->
            selectorMonth.value?.let { month ->
                selectorDay.value?.let {
                    //  sechStartTime 每天的开始时间：例如：2020-04-01 00:00:00的时间戳
                    val sechStartTime = TimeUtil.getMilliStringByYearMonthDay(year, month, it)
                    initiateRequestNotState {
//                        Log.d("wyy", "获取用户某日，所有日程 monoId:$monoId")
                        val httpBaseBean = mRepository.remoteSchedulesOnDay(monoId, sechStartTime = sechStartTime)
                        if (httpBaseBean.code == 8000) {
                            mSchedules.postValue(httpBaseBean.body)
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
        titleOfYear.value?.let { year ->
            titleOfMonth.value?.let { month ->
                //  sechStartTime 每天的开始时间：例如：2020-04-01 00:00:00的时间戳
                val sechStartTime = TimeUtil.getMilliStringByYearMonthDay(year, month, 1)
                jobOfDaysByMonth?.cancel()
                jobOfDaysByMonth = viewModelScope.launch {
//                    Log.d("wyy", "获取某月内，用户所有日程管理的日期集合 monoId:$monoId")
                    val httpBaseBean = mRepository.remoteDaysByTheMonth(monoId, sechStartTime = sechStartTime)
                    if (httpBaseBean.code == 8000) {
                        mBodyOnDaysByTheMonthEntities.postValue(httpBaseBean.body)
                    }
                }
            }
        }
    }
}