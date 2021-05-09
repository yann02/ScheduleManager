package com.shkj.cm

import androidx.lifecycle.MutableLiveData
import com.shkj.cm.base.repository.ApiRepository
import com.shkj.cm.common.state.State
import com.shkj.cm.modules.main.entities.request.Body
import com.shkj.cm.modules.main.entities.request.BodyByDays
import com.shkj.cm.modules.main.entities.request.DaySchedulesEntity
import com.shkj.cm.modules.main.entities.request.DaysByTheMonthEntity
import com.shkj.cm.modules.main.entities.result.DaySchedulesResultEntity
import com.shkj.cm.modules.main.entities.result.DaysByTheMonthResultEntity

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/16 22:58
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class MainRepository(var loadState: MutableLiveData<State>) : ApiRepository() {
    /**
     * 获取用户某日，所有日程
     * @param monoId 用户Id
     * @param sechStartTime 每天的开始时间：例如：2020-04-01 00:00:00的时间戳
     */
    suspend fun remoteSchedulesOnDay(monoId: String, sechStartTime: String): DaySchedulesResultEntity {
        return apiService.remoteSchedulesOnDay(DaySchedulesEntity(Body(monoId, sechStartTime)))
    }

    /**
     * 获取某月内，用户所有日程管理的日期集合
     * @param monoId 用户Id
     * @param sechStartTime 每月开始时间：例如：2020-04-01 00:00:00的时间戳
     */
    suspend fun remoteDaysByTheMonth(monoId: String, sechStartTime: String): DaysByTheMonthResultEntity {
        return apiService.remoteDaysByTheMonth(DaysByTheMonthEntity(BodyByDays(monoId, sechStartTime)))
    }
}