package com.shkj.cm.network

import com.shkj.cm.RequestEntityOfList
import com.shkj.cm.base.models.HttpBaseBean
import com.shkj.cm.common.symbols.ConstantUrls
import com.shkj.cm.modules.detail.entity.request.ScheduleByIdEntity
import com.shkj.cm.modules.detail.entity.result.ScheduleByIdResultEntity
import com.shkj.cm.modules.form.entities.request.add.RequestEntityForAddSchedule
import com.shkj.cm.modules.form.entities.request.add.RequestEntityForEditSchedule
import com.shkj.cm.modules.form.entities.result.add.ResultEntityForAddSchedule
import com.shkj.cm.modules.form.entities.result.add.ResultEntityForEditSchedule
import com.shkj.cm.modules.list.entities.request.DeleteAllOfSchedulesForRequestEntity
import com.shkj.cm.modules.list.entities.request.DeleteScheduleForRequestEntity
import com.shkj.cm.modules.list.entities.result.DeleteAllOfSchedulesForResultEntity
import com.shkj.cm.modules.list.entities.result.DeleteScheduleForResultEntity
import com.shkj.cm.modules.list.entities.result.ListEntityForResult
import com.shkj.cm.modules.main.entities.request.DaySchedulesEntity
import com.shkj.cm.modules.main.entities.request.DaysByTheMonthEntity
import com.shkj.cm.modules.main.entities.result.DaySchedulesResultEntity
import com.shkj.cm.modules.main.entities.result.DaysByTheMonthResultEntity
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 接口配置服务类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/1/21 17:26
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
interface ApiService {

    /**
     * 添加日程
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_ADD_SCHEDULE)
    suspend fun userScheAddTime(@Body entity: RequestEntityForAddSchedule): ResultEntityForAddSchedule

    /**
     * 编辑日程
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_EDIT_SCHEDULE)
    suspend fun userScheEditTime(@Body entity: RequestEntityForEditSchedule): ResultEntityForEditSchedule

    /**
     * 获取用户的所有日程(分页)
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_GET_ALL_OF_SCHEDULE_ON_PAGE)
    suspend fun getMeetUserScheManagePage(@Body entity: RequestEntityOfList): ListEntityForResult

    /**
     * 删除单个用户日程
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_DELETE_SCHEDULE)
    suspend fun deleteSchedule(@Body entity: DeleteScheduleForRequestEntity): DeleteScheduleForResultEntity

    /**
     * 清空用户所有日程
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_DELETE_ALL_OF_SCHEDULES)
    suspend fun deleteAllOfSchedules(@Body entity: DeleteAllOfSchedulesForRequestEntity): DeleteAllOfSchedulesForResultEntity

    /**
     * 获取用户某日，所有日程
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_GET_SCHEDULES_ON_DAY)
    suspend fun remoteSchedulesOnDay(@Body entity: DaySchedulesEntity): DaySchedulesResultEntity

    /**
     * 获取某月内，用户所有日程管理的日期集合
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_GET_DAYS_BY_MONTH)
    suspend fun remoteDaysByTheMonth(@Body entity: DaysByTheMonthEntity): DaysByTheMonthResultEntity


    /**
     * 获取某月内，用户所有日程管理的日期集合
     */
    @JvmSuppressWildcards
    @POST(ConstantUrls.URL_GET_SCHEDULE_BY_ID)
    suspend fun remoteSchedulesById(@Body entity: ScheduleByIdEntity): ScheduleByIdResultEntity
}