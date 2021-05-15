package com.shkj.cm.modules.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.shkj.cm.MainViewModel
import com.shkj.cm.R
import com.shkj.cm.base.navController
import com.shkj.cm.base.view.BaseLifeCycleFragment
import com.shkj.cm.common.Constant
import com.shkj.cm.common.symbols.ConstantRouterParamKey
import com.shkj.cm.databinding.FragmentDetailBinding
import com.shkj.cm.modules.detail.entity.ScheduleEntity
import com.xuexiang.xutil.data.DateUtils


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @description: 日程详情
 * @author: ljp
 * @dateTime: 2021/4/23 15:15
 */
class DetailFragment : BaseLifeCycleFragment<DetailViewModel, FragmentDetailBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_detail
    private val viewModelOfMainActivity by activityViewModels<MainViewModel>()
    var tId = ""
    override fun initView() {
        super.initView()
        showSuccess()

        mDataBinding.ibOnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        mDataBinding.btnEdit.setOnClickListener {
            navController(this,R.id.action_detailFragment_to_formFragment,Bundle().apply {
                putBoolean(
                    ConstantRouterParamKey.IS_EDIT,
                    true
                );putParcelable(ConstantRouterParamKey.SCHEDULE_ENTITY, scheduleEntity)
            })
//            findNavController().navigate(R.id.action_detailFragment_to_formFragment,
//                Bundle().apply {
//                    putBoolean(
//                        ConstantRouterParamKey.IS_EDIT,
//                        true
//                    );putParcelable(ConstantRouterParamKey.SCHEDULE_ENTITY, scheduleEntity)
//                })
        }
    }

    override fun onResume() {
        super.onResume()
        tId = arguments?.getString(ConstantRouterParamKey.TID)!!
        com.xuexiang.xutil.common.logger.Logger.dTag("dosmono", "tid=$tId")
        mViewModel.remoteSchedulesById(tId)
    }
    lateinit var scheduleEntity: ScheduleEntity
    override fun initDataObserver() {
        mViewModel.remoteSuccess.observe(this, Observer {
            if (it?.body == null) {
                showEmpty()
                return@Observer
            }
            scheduleEntity = ScheduleEntity(
                it.body.createtime,
                it.body.dtag,
                it.body.endTime,
                it.body.freq.toInt(),
                it.body.gmt,
                viewModelOfMainActivity.monoId,
                it.body.startTime,
                it.body.tid,
                it.body.title,
                it.body.preTime
            )
            mDataBinding.tvSubtitleValue.text = if(it.body.title.length <= 20) it.body.title else it.body.title.substring(0,20)+"..."
            mDataBinding.tvRepeatValue.text = when (it.body.freq) {
                Constant.RULE_BY_DAY -> Constant.CHAR_BY_DAY
                Constant.RULE_BY_MONTH -> Constant.CHAR_BY_MONTH
                Constant.RULE_BY_WEEK -> Constant.CHAR_BY_WEEK
                Constant.RULE_BY_YEAR -> Constant.CHAR_BY_YEAR
                else -> Constant.CHAR_NO_REPEAT
            }
            mDataBinding.tvTimeValue.text =
                if (it.body.dtag == 1) Constant.CHAR_NO_DAY else Constant.CHAR_DAY
            //设置开始时间
            mDataBinding.tvStartTimeValue.text = if (it.body.dtag == 1)
                DateUtils.millis2String(it.body.startTime.toLong(),DateUtils.yyyyMMddHHmm.get())
            else DateUtils.millis2String(it.body.startTime.toLong(),DateUtils.yyyyMMdd.get())
            //设置结束时间
            mDataBinding.tvEndTimeValue.text = if (it.body.dtag == 1)
                DateUtils.millis2String(it.body.endTime.toLong(),DateUtils.yyyyMMddHHmm.get())
            else DateUtils.millis2String(it.body.endTime.toLong(),DateUtils.yyyyMMdd.get())

            mDataBinding.tvTimezoneValue.text = it.body.gmt
            repeat(it.body.preTime.size) { index ->
                arrayOf(
                    mDataBinding.tvFrequencyValue1,
                    mDataBinding.tvFrequencyValue2,
                    mDataBinding.tvFrequencyValue3,
                    mDataBinding.tvFrequencyValue4,
                    mDataBinding.tvFrequencyValue5
                )[index].let { view ->
                    view.visibility = View.VISIBLE
                    view.text = when (it.body.preTime[index]) {
                        Constant.VALUE_ADVANCE_FIVE_MINUTES -> Constant.CHAR_FIVE_MINUTE
                        Constant.VALUE_ADVANCE_TEN_MINUTES -> Constant.CHAR_TEN_MINUTE
                        Constant.VALUE_ADVANCE_FIFTH_MINUTES -> Constant.CHAR_FIFTY_MINUTE
                        Constant.VALUE_ADVANCE_THIRTY_MINUTES -> Constant.CHAR_THIRTY_MINUTE
                        Constant.VALUE_ADVANCE_A_HOUR -> Constant.CHAR_A_HOUR
                        Constant.VALUE_ADVANCE_TWO_HOUR -> Constant.CHAR_TWO_HOUR
                        Constant.VALUE_ADVANCE_A_DAY -> Constant.CHAR_ADVANCE_A_DAY
                        Constant.VALUE_ADVANCE_A_WEEK -> Constant.CHAR_ADVANCE_A_WEEK
                        Constant.VALUE_ADVANCE_TWO_DAY -> Constant.CHAR_ADVANCE_TWO_DAY
                        else -> Constant.CHAR_TODAY
                    }
                }
            }
        })
    }
}