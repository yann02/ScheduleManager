package com.shkj.cm.modules.detail

import androidx.lifecycle.MutableLiveData
import com.shkj.cm.base.repository.ApiRepository
import com.shkj.cm.base.repository.BaseRepository
import com.shkj.cm.common.state.State
import com.shkj.cm.modules.detail.entity.request.BodyById
import com.shkj.cm.modules.detail.entity.request.ScheduleByIdEntity
import com.shkj.cm.modules.detail.entity.result.ScheduleByIdResultEntity
import com.shkj.cm.modules.main.entities.request.BodyByDays
import com.shkj.cm.modules.main.entities.request.DaysByTheMonthEntity
import com.shkj.cm.modules.main.entities.result.DaysByTheMonthResultEntity

class DetailRepository(var loadState: MutableLiveData<State>) : ApiRepository() {

    /**
     * 获取某月内，用户所有日程管理的日期集合
     * @param monoId 用户Id
     * @param sechStartTime 每月开始时间：例如：2020-04-01 00:00:00的时间戳
     */
    suspend fun remoteSchedulesById(tId: String): ScheduleByIdResultEntity {
        return apiService.remoteSchedulesById(ScheduleByIdEntity(BodyById(tId)))
    }
}