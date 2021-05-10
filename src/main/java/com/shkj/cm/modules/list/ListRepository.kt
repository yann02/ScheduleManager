package com.shkj.cm.modules.list

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shkj.cm.Body
import com.shkj.cm.RequestEntityOfList
import com.shkj.cm.base.repository.ApiRepository
import com.shkj.cm.common.state.State
import com.shkj.cm.db.RoomHelper
import com.shkj.cm.modules.list.entities.request.BodyDeleteAllOfSchedulesForRequestEntity
import com.shkj.cm.modules.list.entities.request.BodyDeleteScheduleForRequestEntity
import com.shkj.cm.modules.list.entities.request.DeleteAllOfSchedulesForRequestEntity
import com.shkj.cm.modules.list.entities.request.DeleteScheduleForRequestEntity
import com.shkj.cm.modules.list.entities.result.Data
import com.shkj.cm.modules.list.entities.result.DeleteAllOfSchedulesForResultEntity
import com.shkj.cm.modules.list.entities.result.DeleteScheduleForResultEntity
import com.shkj.cm.modules.list.entities.result.ListEntityForResult
import kotlinx.coroutines.flow.Flow

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程分页列表
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/23 11:06
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class ListRepository(var loadState: MutableLiveData<State>) : ApiRepository() {

    /**
     * 分页获取日程列表
     * @param curPage 查找的页数
     * @param monoId 用户Id
     * @param pageSize 每页多少条数据
     */
    suspend fun getMeetUserScheManagePage(curPage: Int = 1, monoId: String, pageSize: Int = 10): ListEntityForResult {
        return apiService.getMeetUserScheManagePage(RequestEntityOfList(Body(curPage.toString(), monoId, pageSize.toString())))
    }

    /**
     * 删除单个用户日程
     * @param tid 日程唯一标识ID
     */
    suspend fun deleteSchedule(tid: String): DeleteScheduleForResultEntity {
        return apiService.deleteSchedule(DeleteScheduleForRequestEntity(BodyDeleteScheduleForRequestEntity(tid)))
    }

    /**
     * 删除用户所有日程
     * @param monoId 用户ID
     */
    suspend fun deleteAllOfSchedules(monoId: String): DeleteAllOfSchedulesForResultEntity {
        return apiService.deleteAllOfSchedules(
            DeleteAllOfSchedulesForRequestEntity(
                BodyDeleteAllOfSchedulesForRequestEntity(
                    monoId
                )
            )
        )
    }

    fun getSearchResultStream(monoId: String): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SchedulePagingSource(this, monoId) }
        ).flow
    }

    /**
     * 获取日程列表（room+retrofit）
     */
    fun getScheduleResultStream(monoId: String): Flow<PagingData<Data>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ListPageRemoteMediator(this, monoId),
            pagingSourceFactory = { RoomHelper.listPageDao?.pagingSource()!! }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 3
    }
}