package com.shkj.cm.modules.form

import android.Manifest
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dosmono.platecommon.util.UIUtils
import com.permissionx.guolindev.PermissionX

import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.common.calendar.CalendarProviderManager
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.common.util.*
import com.shkj.cm.databinding.FragmentFormBinding
import com.shkj.cm.modules.detail.entity.ScheduleEntity
import com.shkj.cm.widgets.FrequencyView
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.picker.widget.TimePickerView
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener
import com.xuexiang.xutil.data.DateUtils
import kotlinx.android.synthetic.main.fragment_form2.*
import java.util.*


class FormFragment : BaseLifeCycleFragment<FormViewModel, FragmentFormBinding>() {
    /**
     * Copyright (C), 2015-2021, 海南双猴科技有限公司
     * @Description: 日程表单页面（新增、编辑）
     * @Author: Yingyan Wu
     * @CreateDate: 2021/4/21 21:14
     * History:
     * @Author: 暂无
     * @Date: 暂无
     * @Description: 暂无
     */
    private var mStartTimePicker: TimePickerView? = null
    private var mEndTimePicker: TimePickerView? = null
    override fun getLayoutId() = R.layout.fragment_form
    private val viewModelOfMainActivity by activityViewModels<MainViewModel>()

    var scheduleEntity: ScheduleEntity? = null
    override fun initView() {
        super.initView()
        showSuccess()
        mViewModel.monoId = viewModelOfMainActivity.monoId

//        Permissions(this).request(
//            Manifest.permission.READ_CALENDAR,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_CALENDAR
//        )

        requestPermission()
        //  监听用户点击返回
        mDataBinding.ibOnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        //  监听用户点击保存按钮
        mDataBinding.btnSave.setOnClickListener {
            //  用户点击了保存按钮
            //  添加日程
            if (mViewModel.isEdit) mViewModel.onEditSchedule()
            else mViewModel.onAddSchedule()
        }
        //  监听标题输入
        mDataBinding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //  标题文本发生变化，刷新标题值
                mViewModel.title.postValue(p0.toString())
            }
        })
        //  监听是否全天
        mDataBinding.sbAllOfDay.setOnCheckedChangeListener { _, isChecked ->
            //  更新标记值
            mViewModel.isEditInit = false
            mViewModel.dtag.postValue(getDtagValue(isChecked))

        }
        //  监听选择开始的时间
        mDataBinding.tvStartTime.setOnClickListener {
            showTimePickerForStartTime()
        }
        //  监听选择结束的时间
        mDataBinding.tvEndTime.setOnClickListener {
            showTimePickerForEndTime()
        }
        mDataBinding.tvNotRepeatContent.setOnItemSelectedListener { view, position, id, item ->
            mViewModel.freq.postValue("$position")
        }

        //初始化频次下拉选择事件
        initFrequencySelectListener(
            mDataBinding.frequencyView1,
            mDataBinding.frequencyView2,
            mDataBinding.frequencyView3,
            mDataBinding.frequencyView4,
            mDataBinding.frequencyView5
        ) { view, i ->

            updateFrequencyValue(view, i + 1)
        }
        frequencyView1.bindingAddFrequencyCallback {

            //更新频次
            addFrequencyView()?.let { frequencyView ->
                updateFrequencyValue(frequencyView)
            }
        }
        bindingDeleteFrequencyCallback(
            mDataBinding.frequencyView2,
            mDataBinding.frequencyView3,
            mDataBinding.frequencyView4,
            mDataBinding.frequencyView5
        ) {

//            if(mViewModel.isEdit){
//                mViewModel.deleteCalendarEvent(
//                    requireContext(),
//                    "${it.getSelectIndex() + 1}"
//                )
//            }
            //更新频次
            updateFrequencyValue(it, -1)
            //重置选项
            it.resetSelectedIndex()
        }
        //初始化隐藏其他下拉框
        hideAnotherFrequencyView()

        arguments?.apply {
            if (getBoolean(ConstantRouterParamKey.IS_EDIT)) {
                mDataBinding.tvTitle.text =
                    resources.getString(R.string.title_of_edit)
            }
            scheduleEntity = getParcelable<ScheduleEntity>(ConstantRouterParamKey.SCHEDULE_ENTITY)
            initScheduleInfo()
        }
    }

    /**
     * 显示选择开始时间的弹出框
     */
    private fun showTimePickerForStartTime() {
        mStartTimePicker?.show()
    }

    /**
     * 初始化开始时间的时间选择器
     */
    private fun initStartTimePicker(mTimePickerType: TimePickerType) {
        mStartTimePicker = TimePickerBuilder(context,
            OnTimeSelectListener { date, _ ->
                //  用户选择了某个开始的时间
                onChangeByStartTime(date)
            })
            .setTimeSelectChangeListener { Log.i("pvTime", "onTimeSelectChanged") }
            .setType(mTimePickerType)
            .setTitleText(context?.getString(R.string.selected_for_start_time))
            .isDialog(false)
            .setOutSideCancelable(false)
            .setDate(getStartCalendar())
            .build()
    }

    /**
     * 获取开始的calendar
     */
    private fun getStartCalendar() = if (mViewModel.startTimeStr.isEmpty()) {
        TimeUtil.getCalendar0fStartTimeOfNotFullDayOnDefault()
    } else {
        TimeUtil.getCalendarByMillisecondString(mViewModel.startTimeStr)

    }


    /**
     * 用户选择了某个开始的时间
     */
    private fun onChangeByStartTime(date: Date) {
        var tempStartTimeOnFormat = DateUtils.date2String(
            date,
            mViewModel.mDateForm.value
        )
        if (!checkTimeValidity(tempStartTimeOnFormat, mViewModel.endTimeOnFormat.value!!)) {
            MaterialDialog.Builder(requireContext())
                .title(R.string.check_time_tip)
                .content(R.string.check_time_content)
                .positiveText(R.string.check_time_close)
                .show()
            return
        }
        mViewModel.startTimeOnFormat.postValue(
            DateUtils.date2String(
                date,
                mViewModel.mDateForm.value
            )
        )
        mViewModel.startTime.postValue(DateUtils.date2Millis(date).toString())
        setRepeatLevel(tempStartTimeOnFormat, mViewModel.endTimeOnFormat.value!!)
    }

    /**
     * 显示选择结束时间的弹出框
     */
    private fun showTimePickerForEndTime() {
        mEndTimePicker?.show()
    }


    /**
     * 初始化结束时间的时间选择器
     */
    private fun initEndTimePicker(mTimePickerType: TimePickerType) {
        mEndTimePicker = TimePickerBuilder(context,
            OnTimeSelectListener { date, _ ->
                //  用户选择了某个结束的时间
                onChangeByEndTime(date)
            })
            .setTimeSelectChangeListener { Log.i("pvTime", "onTimeSelectChanged") }
            .setType(mTimePickerType)
            .setTitleText(context?.getString(R.string.selected_for_end_time))
            .isDialog(false)
            .setOutSideCancelable(false)
            .setDate(getEndCalendar())
            .build()
    }

    /**
     * 获取结束时间的Calendar
     */
    private fun getEndCalendar() = if (mViewModel.endTimeStr.isEmpty()) {
        TimeUtil.getCalendar0fEndTimeOfNotFullDayOnDefault()
    } else {
        TimeUtil.getCalendarByMillisecondString(mViewModel.endTimeStr)
    }

    /**
     * 用户选择了某个结束的时间
     */
    private fun onChangeByEndTime(date: Date) {
        var tempEndTimeOnFormat = DateUtils.date2String(
            date,
            mViewModel.mDateForm.value
        )
        if (!checkTimeValidity(mViewModel.startTimeOnFormat.value!!, tempEndTimeOnFormat)) {
            MaterialDialog.Builder(requireContext())
                .title(R.string.check_time_tip)
                .content(R.string.check_time_content)
                .positiveText(R.string.check_time_close)
                .show()
            return
        }
        mViewModel.endTimeOnFormat.postValue(tempEndTimeOnFormat)
        mViewModel.endTime.postValue(DateUtils.date2Millis(date).toString())
        setRepeatLevel(mViewModel.startTimeOnFormat.value!!, tempEndTimeOnFormat)
    }

    //检查起始时间的有效性
    private fun checkTimeValidity(startDate: String, endDate: String): Boolean {

        var startMillis = DateUtils.string2Millis(startDate, mViewModel.mDateForm.value)
        var endMillis = DateUtils.string2Millis(endDate, mViewModel.mDateForm.value)
        return startMillis < endMillis
    }


    //选择重复的类型
    private fun setRepeatLevel(startDate: String, endDate: String): List<String> {
        var start = DateUtils.string2Date(startDate, mViewModel.getDateForm())
        var end = DateUtils.string2Date(endDate, mViewModel.getDateForm())
        return when {
            //每年
            DateUtils.getYear(end) > DateUtils.getYear(start) -> {
                mViewModel.repeatLevel.value = R.array.every_year_array
                ResUtils.getStringArray(R.array.every_year_array).toList()
            }
            //每月
            DateUtils.getMonth(end) > DateUtils.getMonth(start) -> {
                mViewModel.repeatLevel.value = R.array.every_month_array
                ResUtils.getStringArray(R.array.every_month_array).toList()
            }
            //每周
            getWeekOfMonth(end) > getWeekOfMonth(start) -> {
                mViewModel.repeatLevel.value = R.array.every_week_array

                ResUtils.getStringArray(R.array.every_week_array).toList()
            }
            //每天
            DateUtils.getDay(end) > DateUtils.getDay(start) -> {
                mViewModel.repeatLevel.value = R.array.every_day_array
                ResUtils.getStringArray(R.array.every_day_array).toList()
            }
            else -> {
                mViewModel.repeatLevel.value = R.array.no_repeat_array
                ResUtils.getStringArray(R.array.no_repeat_array).toList()
            }
        }
    }

    /**
     * 获取标记值
     * 为真时（全天）：2
     * 为假时（非全天）：1
     */
    private fun getDtagValue(checked: Boolean): String? {
        return if (checked) DTAG_FULL_OF_DAY else DTAG_NOT_FULL_OF_DAY
    }

    /**
     * 监听ViewModel数据的变化
     */
    override fun initDataObserver() {
        super.initDataObserver()
        mDataBinding.vm = mViewModel
        mViewModel.mTimePickerType.observe(this, androidx.lifecycle.Observer {
            initStartTimePicker(it)
            initEndTimePicker(it)
        })
        mViewModel.mDateForm.observe(this, androidx.lifecycle.Observer {
        })
        mViewModel.repeatLevel.observe(this, androidx.lifecycle.Observer {
            mDataBinding.tvNotRepeatContent.setItems(ResUtils.getStringArray(it).toList())
        })
        mViewModel.success.observe(this, androidx.lifecycle.Observer {
            if (it) {
                if (mViewModel.isEdit)
                    mViewModel.updateCalendarEvent(requireContext())
                else
                    mViewModel.addCalendarEvent(requireContext())
                findNavController().popBackStack()
//                var eventId = SharedPreUtils.getLong(UIUtils.getContext(), "tempEventId", 0L)
//                CalendarProviderManager.startCalendarForIntentToEdit(requireContext(), eventId)
            }
        })

    }

    private fun addFrequencyView(): FrequencyView? {
        for (frequencyView in arrayOf(
            mDataBinding.frequencyView2,
            mDataBinding.frequencyView3,
            mDataBinding.frequencyView4,
            mDataBinding.frequencyView5
        )) {
            if (!frequencyView.isVisible) {
                frequencyView.visibility = View.VISIBLE
                return frequencyView
            }
        }
        return null
    }

    private fun hideAnotherFrequencyView() {
        mDataBinding.frequencyView2.visibility = View.GONE
        mDataBinding.frequencyView3.visibility = View.GONE
        mDataBinding.frequencyView4.visibility = View.GONE
        mDataBinding.frequencyView5.visibility = View.GONE
    }

    /**
     * 更新频次
     */
    private fun updateFrequencyValue(view: FrequencyView, value: Int = 1) {
        when (view) {
            mDataBinding.frequencyView1 ->
                mViewModel.frequencyValue[0] = "$value"
            mDataBinding.frequencyView2 ->
                mViewModel.frequencyValue[1] = "$value"
            mDataBinding.frequencyView3 ->
                mViewModel.frequencyValue[2] = "$value"
            mDataBinding.frequencyView4 ->
                mViewModel.frequencyValue[3] = "$value"
            mDataBinding.frequencyView5 ->
                mViewModel.frequencyValue[4] = "$value"
        }
    }

    private fun initScheduleInfo() {

        scheduleEntity?.apply {
            mViewModel.isEditInit = true
            mViewModel.editScheduleEntity.postValue(this)
            mViewModel.tid = tid!!
            mViewModel.isEdit = true
            mDataBinding.etTitle.setText(title)
            mViewModel.title.postValue(title)
            mDataBinding.tvTimeZone.text = gmt
            mViewModel.gmt.postValue(gmt)
            mDataBinding.sbAllOfDay.isChecked = dtag == 2
            mViewModel.freq.postValue("$freq")
            mDataBinding.tvEndTime.text = endTime
            startTime?.apply {
                initStartTime(DateUtils.millis2Date(this.toLong()), this)
            }

            endTime?.apply {
                initEndTime(DateUtils.millis2Date(this.toLong()), this)
            }

            mDataBinding.tvNotRepeatContent.setItems(
                setRepeatLevel(
                    mViewModel.startTimeOnFormat.value!!,
                    mViewModel.endTimeOnFormat.value!!
                )
            ).selectedIndex = freq

            preTime?.apply {
                repeat(this.size) { index ->
                    arrayOf(
                        frequencyView1,
                        frequencyView2,
                        frequencyView3,
                        frequencyView4,
                        frequencyView5
                    )[index].let { view ->
                        view.visibility = View.VISIBLE
                        view.setSelectedIndex(preTime!![index].toInt() - 1)
                        mViewModel.frequencyValue[index] = preTime!![index]
                    }
                }
            }

        }
    }

    private fun initStartTime(date: Date, timeMills: String) {

        var tempStartTimeOnFormat = DateUtils.date2String(
            date,
            mViewModel.getDateForm()
        )
        mViewModel.startTimeOnFormat.value = tempStartTimeOnFormat
        mViewModel.startTime.value = timeMills
        mDataBinding.tvStartTime.text = tempStartTimeOnFormat
        mViewModel.startTimeStr = timeMills
    }

    private fun initEndTime(date: Date, timeMills: String) {
        var tempEndTimeOnFormat = DateUtils.date2String(
            date,
            mViewModel.getDateForm()
        )
        mViewModel.endTimeOnFormat.value = tempEndTimeOnFormat
        mDataBinding.tvEndTime.text = tempEndTimeOnFormat
        mViewModel.endTime.value = timeMills
        mViewModel.endTimeStr = timeMills
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CALENDAR
            )
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String?>? ->
                //  解释请求权限的原因
                val message = resources.getString(R.string.explain_message)
                val ok = resources.getString(R.string.explain_ok)
                scope.showRequestReasonDialog(deniedList, message, ok)
            }.onForwardToSettings { scope: ForwardScope, deniedList: List<String?>? ->
                //  提示到设置页面设置权限
                val message = resources.getString(R.string.forward_to_setting_message)
                val ok = resources.getString(R.string.explain_ok)
                scope.showForwardToSettingsDialog(deniedList, message, ok)
            }
            .request { _, _, _ ->
            }
    }
}