package com.shkj.cm.modules.form

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.dosmono.logger.Logger
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.R
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.Constant
import com.shkj.cm.common.calendar.AdvanceTime
import com.shkj.cm.common.calendar.CalendarEvent
import com.shkj.cm.common.calendar.CalendarProviderManager
import com.shkj.cm.common.calendar.RRuleConstant
import com.shkj.cm.common.initiateRequestNotState
import com.shkj.cm.common.util.SharedPreUtils
import com.shkj.cm.common.util.TimeUtil
import com.shkj.cm.db.FrequencyEntity
import com.shkj.cm.db.RemoteKeys
import com.shkj.cm.db.RoomHelper
import com.shkj.cm.modules.detail.entity.ScheduleEntity
import com.shkj.cm.modules.form.entities.result.add.Body
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType
import com.xuexiang.xutil.data.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: viewmodel处理业务逻辑
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/21 16:50
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
const val DTAG_NOT_FULL_OF_DAY = "1"
const val DTAG_FULL_OF_DAY = "2"

class FormViewModel : BaseViewModel<FormRepository>() {
    //  标题
    var title = MutableLiveData<String>()

    //  时区
    var gmt = MutableLiveData<String>("GMT@8:00")

    //  标记（2:全天、1:非全天）
    var dtag = MutableLiveData<String>(DTAG_NOT_FULL_OF_DAY)

    //  开始时间（全天：yyyy-mm-dd 非全天：yyyy-mm-dd hh:mm）
    var startTimeOnFormat = MutableLiveData<String>()

    //  结束时间（全天：yyyy-mm-dd 非全天：yyyy-mm-dd hh:mm）
    var endTimeOnFormat = MutableLiveData<String>()

    //  开始时间（13位时间戳）
    var startTime = MutableLiveData<String>()

    //  结束时间（13位时间戳）
    var endTime = MutableLiveData<String>()

    var startTimeStr = ""
    var endTimeStr = ""
    var isEdit = MutableLiveData(false)

    //    var isEdit = false
    var isVoiceAdd = false
    var tid = ""
    val editScheduleEntity = MutableLiveData<ScheduleEntity>()
    var isEditInit = false
    val mDateForm = Transformations.map(dtag) {
        val dateForm: DateFormat
        if (it == DTAG_NOT_FULL_OF_DAY) {
            //  非全天
            dateForm = DateUtils.yyyyMMddHHmm.get()!!
            mTimePickerType.postValue(TimePickerType.DATE)
            refreshTimeForNotFullOfDay(dateForm)
        } else {
            //  全天
            dateForm = DateUtils.yyyyMMdd.get()!!
            mTimePickerType.postValue(TimePickerType.DEFAULT)
            refreshTimeForFullOfDay(dateForm)
        }
        dateForm
    }

    var mTimePickerType = MutableLiveData<TimePickerType>()
    fun updateDateForm(coverValue: Boolean = true) {
        if (dtag.value == DTAG_NOT_FULL_OF_DAY) {
            //  非全天
            mTimePickerType.postValue(TimePickerType.DATE)
            refreshTimeForNotFullOfDay(mDateForm.value!!)
        } else {
            //  全天
            mTimePickerType.postValue(TimePickerType.DEFAULT)
            refreshTimeForFullOfDay(mDateForm.value!!)
        }
    }

    /**
     * 刷新时间（非全天转全天）
     */
    private fun refreshTimeForFullOfDay(dateForm: DateFormat) {
        //判断 是否 编辑初始化，是 不用下面的操作
        if (isEditInit || isVoiceAdd) return
        val startFormatString = if (startTimeOnFormat.value != null) {
            TimeUtil.notFullOfDayToFullOfDay(startTimeOnFormat.value!!)
        } else {
            TimeUtil.getFullOfDay()
        }
        val endFormatString = if (endTimeOnFormat.value != null) {
            TimeUtil.notFullOfDayToFullOfDay(endTimeOnFormat.value!!)
        } else {
            TimeUtil.getFullOfDayForEndTime()
        }

        startTimeOnFormat.postValue(startFormatString)
        endTimeOnFormat.postValue(endFormatString)
        startTime.postValue(DateUtils.string2Millis(startFormatString, dateForm).toString())
//        startTime.postValue("${TimeUtil.datePlus9Hours2Millis(DateUtils.string2Date(startFormatString,dateForm))}")
//        endTime.postValue("${TimeUtil.datePlus10Hours2Millis(DateUtils.string2Date(endFormatString,dateForm))}")
        endTime.postValue(DateUtils.string2Millis(endFormatString, dateForm).toString())
    }

    /**
     * 刷新时间（全天转非全天）
     */
    private fun refreshTimeForNotFullOfDay(dateForm: DateFormat) {
        //判断 是否 编辑初始化，是 不用下面的操作
        if (isEditInit) return
        val startFormatString = if (startTimeOnFormat.value != null) {
            TimeUtil.fullOfDayToNotFullOfDay(startTimeOnFormat.value!!)
        } else {
            TimeUtil.getNotFullOfDay()
        }
        val endFormatString = if (endTimeOnFormat.value != null) {
            TimeUtil.fullOfDayToNotFullOfDay(endTimeOnFormat.value!!)
        } else {
            TimeUtil.getNotFullOfDayForEndTime()
        }

        startTimeOnFormat.postValue(startFormatString)
        endTimeOnFormat.postValue(endFormatString)
        startTime.postValue(DateUtils.string2Millis(startFormatString, dateForm).toString())
        endTime.postValue(DateUtils.string2Millis(endFormatString, dateForm).toString())
    }

    //  重复(0，不重复；1，每天；2，每周；3，每月；4，每年)
    var freq = MutableLiveData<String>("0")

    //  用户ID
    var monoId: String = ""

    //  频次(1, 5分钟；2, 10分钟；3, 15分钟；4，30分钟；5, 1小时；6,  2小时；7，当天；8，1天前；,9, 2天前；10，1周前；)
    var preTime = MutableLiveData<MutableList<String>>(mutableListOf())

    var noFullDayFrequencyValue = mutableListOf("1", "-1", "-1", "-1", "-1")
    var fullDayFrequencyValue = mutableListOf("7", "-1", "-1", "-1", "-1")
    var repeatLevel = MutableLiveData<@ArrayRes Int>(R.array.no_repeat_array)

    var success = MutableLiveData<Boolean>()

    var deleted = MutableLiveData(false)

    /**
     * 创建日程
     */
    fun onAddSchedule() {
        if (checkSubmitUnable()) {
            //  校验未通过
            //  标题不能为空
            Toast.makeText(
                UIUtils.getContext(),
                UIUtils.getContext().getString(R.string.title_is_empty),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        setPreTimes()
        Logger.d("startTime =${startTime.value},endTime = ${endTime.value}")
        if (dtag.value == DTAG_FULL_OF_DAY) {
            startTime.postValue(
                "${
                    TimeUtil.datePlus9Hours2Millis(
                        DateUtils.string2Date(
                            startTimeOnFormat.value,
                            mDateForm.value
                        )
                    )
                }"
            )
            endTime.postValue("${TimeUtil.datePlus9Hours2Millis(DateUtils.string2Date(endTimeOnFormat.value, mDateForm.value))}")
        }
        initiateRequestNotState {
            val httpBaseBean =
                mRepository.userScheAddTime(
                    dtag.value!!,
                    endTime.value!!,
                    freq.value!!,
                    gmt.value!!,
                    monoId,
                    preTime.value!!,
                    startTime.value!!,
                    title.value!!
                )
            if (httpBaseBean.code == 8000) {
                //  接口返回成功
                Toast.makeText(UIUtils.getContext(), httpBaseBean.msg, Toast.LENGTH_LONG).show()
                //  日程保存到数据库
                addToLocalDB(httpBaseBean.body)
                success.postValue(true)
            }
        }
    }


    fun setPreTimes() {
        preTime.value!!.clear()
        if (dtag.value == DTAG_NOT_FULL_OF_DAY) {
            for (value in noFullDayFrequencyValue) {
                if (value != "-1") {
                    preTime.value!!.add(value)
                }
            }
        } else {
            for (value in fullDayFrequencyValue) {
                if (value != "-1") {
                    preTime.value!!.add(value)
                }
            }
        }
    }

    fun onEditSchedule() {
        if (checkSubmitUnable()) {
            //  校验未通过
            //  标题不能为空
            Toast.makeText(
                UIUtils.getContext(),
                UIUtils.getContext().getString(R.string.title_is_empty),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        setPreTimes()
        //设置时间的早上9点
        if (dtag.value == DTAG_FULL_OF_DAY) {
            startTime.postValue(
                "${
                    TimeUtil.datePlus9Hours2Millis(
                        DateUtils.string2Date(
                            startTimeOnFormat.value,
                            mDateForm.value
                        )
                    )
                }"
            )
            endTime.postValue("${TimeUtil.datePlus9Hours2Millis(DateUtils.string2Date(endTimeOnFormat.value, mDateForm.value))}")
        }
        initiateRequestNotState {
            val httpBaseBean =
                mRepository.userScheEditTime(
                    tid,
                    dtag.value!!,
                    endTime.value!!,
                    freq.value!!,
                    gmt.value!!,
                    monoId,
                    preTime.value!!,
                    startTime.value!!,
                    title.value!!
                )
            if (httpBaseBean.code == 8000) {
                //  接口返回成功
                Toast.makeText(UIUtils.getContext(), httpBaseBean.msg, Toast.LENGTH_LONG).show()
                //  日程保存到数据库
                success.postValue(true)
                editScheduleEntity.value!!.startTime = startTime.value!!
                editScheduleEntity.value!!.dtag = dtag.value!!.toInt()
                editScheduleEntity.value!!.freq = freq.value!!.toInt()
                editScheduleEntity.value!!.gmt = gmt.value
                editScheduleEntity.value!!.title = title.value
                editScheduleEntity.value!!.preTime = preTime.value
                editScheduleEntity.value!!.endTime = endTime.value
//                updateToLocalDB(editScheduleEntity.value!!)
            }
        }
    }

    /**
     * 新建日程提交成功后，将后台返回的对象保存到本地数据库
     */
    private fun addToLocalDB(body: Body) {
//        val entity = com.shkj.cm.db.Body("1618900793724", 1, "1618794000000", 0, "GMT+8:00", "2000206", "1618621200000", "512747712564572161", "测试标题5")
//        val arrayList = arrayListOf("1", "2", "3")
//        entity.preTime = arrayList
        tid = body.tid
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RoomHelper.scheduleDao?.insertSchedule(getLocalBody(body))
            }
        }
    }

    /**
     * 新建日程提交成功后，将后台返回的对象保存到本地数据库
     */
    private fun updateToLocalDB(entity: ScheduleEntity) {
        entity.apply {
            var body = com.shkj.cm.db.Body(
                createtime!!, dtag, endTime!!, freq, gmt!!, monoId!!, startTime!!, tid!!, title!!
            )
            body.preTime = preTime!!
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    RoomHelper.scheduleDao?.updateFrequencyEntitiesOnSchedule(body)
                }
            }
        }


    }


    /**
     * 后台返回的对象转本地对象
     */
    private fun getLocalBody(body: Body): com.shkj.cm.db.Body {
        val entity = com.shkj.cm.db.Body(
            body.createtime,
            body.dtag,
            body.endTime,
            body.freq,
            body.gmt,
            body.monoId,
            body.startTime,
            body.tid,
            body.title
        )
        entity.preTime = body.preTime
        return entity
    }

    /**
     * 校验是否可以提交日程
     */
    private fun checkSubmitUnable() = title.value.isNullOrEmpty()

    fun addCalendarEvent(context: Context) {
        val eventList = mutableListOf<FrequencyEntity>()
        for (frequency in preTime.value!!) {
            var advanceTime = when (frequency) {
                //五分钟前
                Constant.VALUE_ADVANCE_FIVE_MINUTES -> AdvanceTime.FIVE_MINUTES
                //十分钟前
                Constant.VALUE_ADVANCE_TEN_MINUTES -> AdvanceTime.TEN_MINUTES
                //十五分钟前
                Constant.VALUE_ADVANCE_FIFTH_MINUTES -> AdvanceTime.FIFTH_MINUTES
                //三十分钟前
                Constant.VALUE_ADVANCE_THIRTY_MINUTES -> AdvanceTime.THIRTY_MINUTES
                //一小时前
                Constant.VALUE_ADVANCE_A_HOUR -> AdvanceTime.ONE_HOUR
                //两小时前
                Constant.VALUE_ADVANCE_TWO_HOUR -> AdvanceTime.TWO_HOUR
                //一天前
                Constant.VALUE_ADVANCE_A_DAY -> AdvanceTime.ONE_DAY
                //两天前
                Constant.VALUE_ADVANCE_TWO_DAY -> AdvanceTime.TWO_DAY
                //一周前
                Constant.VALUE_ADVANCE_A_WEEK -> AdvanceTime.ONE_WEEK
                //当天
                else -> AdvanceTime.TODAY
            }
            var rrule = when (freq.value!!) {

                Constant.RULE_BY_DAY -> RRuleConstant.REPEAT_CYCLE_DAILY_UNTIL_END_DATE
                Constant.RULE_BY_MONTH -> RRuleConstant.REPEAT_CYCLE_MONTH_UNTIL_END_DATE
                Constant.RULE_BY_WEEK -> RRuleConstant.REPEAT_CYCLE_WEEK_UNTIL_END_DATE
                Constant.RULE_BY_YEAR -> RRuleConstant.REPEAT_CYCLE_YEAR_UNTIL_END_DATE
                else -> ""
            }

            var result = CalendarProviderManager.addCalendarEvent(
                context,
                CalendarEvent(
                    title.value,
                    title.value,
                    "Earth",
                    startTime.value!!.toLong(),
                    endTime.value!!.toLong(),
                    advanceTime,
                    rrule
                )
            )
            Logger.d("add startTime=${startTime.value},endtime = ${endTime.value}")
            if (result == 0) {
                var tempEventId =
                    SharedPreUtils.getLong(UIUtils.getContext(), Constant.TEMP_EVENT_ID, 0L);
                if (tempEventId != 0L) {
//                    eventList.plus(FrequencyEntity(tid, "$advanceTime", tempEventId))
                    eventList.add(FrequencyEntity(tid, "$advanceTime", tempEventId))
                }
            }
        }
        Log.d("wyy", "eventList:$eventList")
        //
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (eventList.isNotEmpty()) {
//                    RoomHelper.scheduleDao?.insertFrequencyForSchedule(eventList)
                    RoomHelper.scheduleDao?.insertFrequenciesForSchedule(eventList)
                }
            }
        }
    }


    fun updateCalendarEvent(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //查询 日程对应的频次，删除对应的日历事件
                RoomHelper.scheduleDao?.queryFrequencyEntitiesOnSchedule(tid)?.apply {
                    forEach {
                        CalendarProviderManager.deleteCalendarEvent(context, it.eventId)
                    }
                }
                //删除日程对应的频次
                RoomHelper.scheduleDao?.deleteFrequencyEntitiesOnSchedule(tid)
                //添加新的日程事件和数据
                addCalendarEvent(context)
            }
        }
    }

    fun deleteCalendarEvent(context: Context, frequency: String) {
        var tempEventId =
            SharedPreUtils.getLong(UIUtils.getContext(), "$tid-${freq.value}-${frequency}", 0L)
        if (tempEventId != 0L)
            CalendarProviderManager.deleteCalendarEvent(context, tempEventId)
    }


    fun getDateForm(): DateFormat {

        return when (dtag.value) {
            DTAG_NOT_FULL_OF_DAY -> {
                //  非全天
                DateUtils.yyyyMMddHHmm.get()!!
            }
            else -> {
                //  全天
                DateUtils.yyyyMMdd.get()!!
            }
        }
    }

    /**
     * 监听用户点击了删除按钮
     */
    fun deleteSchedule(v: View) {
        viewModelScope.launch {
            //  删除一条服务器记录
            val res = mRepository.deleteSchedule(tid)
            if (res.code == 8000) {
                Toast.makeText(UIUtils.getContext(), res.msg, Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO) {
                    //  同步删除本地数据库对应的数据
                    RoomHelper.appDatabase?.withTransaction {
                        val frequencies = RoomHelper.scheduleDao?.queryFrequencyEntitiesOnSchedule(tid)
                        if (!frequencies.isNullOrEmpty()) {
                            for (frequency in frequencies) {
                                CalendarProviderManager.deleteCalendarEvent(UIUtils.getContext(), frequency.eventId)
                            }
                        }
                        //  本地创建数据
                        RoomHelper.scheduleDao?.deleteScheduleTransaction(tid)
                        //  接口获取的分页列表数据
                        RoomHelper.listPageDao?.deleteScheduleTid(tid)
                        RoomHelper.remoteKeysDao?.deleteKeyTid(tid)
                        deleted.postValue(true)
                    }
                }
            }
        }
    }
}