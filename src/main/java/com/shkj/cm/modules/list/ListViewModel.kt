package com.shkj.cm.modules.list

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.calendar.CalendarProviderManager
import com.shkj.cm.db.RemoteKeys
import com.shkj.cm.db.RoomHelper
import com.shkj.cm.modules.list.entities.result.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/23 11:07
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class ListViewModel : BaseViewModel<ListRepository>() {
    var monoId = ""
    var currentScheduleResult: Flow<PagingData<Data>>? = null

    //  上次删除一条日程后是否能取到新数据 默认为true，为false时，下次删除一条日程不必再请求接口获取补充的数据（用于确保分页获取数据时，前后端数据一致）
    var hasSchedule = true

    /**
     * 获取日程列表
     */
    fun findSchedulesByDB(): Flow<PagingData<Data>> {
        val lastResult = currentScheduleResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Data>> = mRepository.getScheduleResultStream(monoId).cachedIn(viewModelScope)
        currentScheduleResult = newResult
        return newResult
    }

    /**
     * 删除一条日程
     */
    fun deleteSchedule(tid: String) {
        viewModelScope.launch {
            //  删除一条服务器记录
            val res = mRepository.deleteSchedule(tid)
            if (res.code == 8000) {
                Toast.makeText(UIUtils.getContext(), res.msg, Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO) {
                    //  同步删除本地数据库对应的数据
                    RoomHelper.appDatabase?.withTransaction {
                        val frequencies = RoomHelper.scheduleDao?.queryFrequencyEntitiesOnSchedule(tid)
                        Log.d("wyy","来了来了")
                        if (!frequencies.isNullOrEmpty()) {
                            for (frequency in frequencies) {
                                Log.d("wyy","eventId:${frequency.eventId}")
                                CalendarProviderManager.deleteCalendarEvent(UIUtils.getContext(), frequency.eventId)
                            }
                        }
                        //  本地创建数据
                        RoomHelper.scheduleDao?.deleteScheduleTransaction(tid)
                        //  接口获取的分页列表数据
                        RoomHelper.listPageDao?.deleteScheduleTid(tid)
                        RoomHelper.remoteKeysDao?.deleteKeyTid(tid)
                    }
                    //  判断是否需要从服务器补充一条数据（同步本地和服务器分页数据，避免数据遗漏）
                    if (hasSchedule) {
                        //  上次删除还能取到新数据
                        //  当前本地有多少条日程数据
                        val page = RoomHelper.listPageDao?.count()
                        withContext(Dispatchers.Default) {
                            page?.let {
                                //  第一页page传1,我们要获取服务器第page+1条数据
                                val response =
                                    mRepository.getMeetUserScheManagePage(curPage = it + 1, monoId = monoId, pageSize = 1)
                                val jobs = response.body.datas
                                hasSchedule = jobs.isNotEmpty()
                                if (hasSchedule) {
                                    //  后台返回的数据不为空
                                    withContext(Dispatchers.IO) {
                                        //  插入补充的一条数据
                                        RoomHelper.appDatabase?.withTransaction {
                                            val lastData = mRepository.getLastSchedule()
                                            val keys: List<RemoteKeys>?
                                            if (lastData != null) {
                                                val lastRemoteKey = RoomHelper.remoteKeysDao?.remoteKeysTId(lastData.tid)
                                                keys = jobs.map { data ->
                                                    RemoteKeys(
                                                        tId = data.tid,
                                                        prevKey = lastRemoteKey?.prevKey,
                                                        nextKey = lastRemoteKey?.nextKey
                                                    )
                                                }
                                            } else {
                                                keys = jobs.map { data ->
                                                    RemoteKeys(
                                                        tId = data.tid,
                                                        prevKey = null,
                                                        nextKey = 2
                                                    )
                                                }
                                            }
                                            RoomHelper.remoteKeysDao?.insertAll(keys)
                                            RoomHelper.listPageDao?.insertAll(jobs)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 清空所有日程
     */
    fun deleteAllOfSchedules() {
        viewModelScope.launch {
            val res = mRepository.deleteAllOfSchedules(monoId)
            if (res.code == 8000) {
                Toast.makeText(UIUtils.getContext(), res.msg, Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO) {
                    RoomHelper.scheduleDao?.deleteAllOfSchedulesTransaction()
                    RoomHelper.remoteKeysDao?.clearRemoteKeys()
                    RoomHelper.listPageDao?.clearAll()
                }
            }
        }
    }
}