package com.shkj.cm.modules.list

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.base.viewmodel.BaseViewModel
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

    //  删除单个日程
    var deleteTag = MutableLiveData<Boolean>()

    //  清空日程
    var deleteAllOfScheduleTag = MutableLiveData<Boolean>()

    /**
     * 获取日程列表
     */
    fun findSchedules(): Flow<PagingData<Data>> {
        val lastResult = currentScheduleResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Data>> = mRepository.getSearchResultStream(monoId).cachedIn(viewModelScope)
        currentScheduleResult = newResult
        return newResult
    }

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
    fun deleteSchedule(tid: String, position: Int) {
        viewModelScope.launch {
            val res = mRepository.deleteSchedule(tid)
            if (res.code == 8000) {
                currentScheduleResult = null
                Toast.makeText(UIUtils.getContext(), res.msg, Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO) {
                    RoomHelper.scheduleDao?.deleteScheduleTransaction(tid)
                    //  通知界面视图刷新界面
                    if (deleteTag.value != null) {
                        deleteTag.postValue(!deleteTag.value!!)
                    } else {
                        deleteTag.postValue(true)
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
                currentScheduleResult = null
                Toast.makeText(UIUtils.getContext(), res.msg, Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO) {
                    RoomHelper.scheduleDao?.deleteAllOfSchedulesTransaction()
                    //  通知界面视图刷新界面
                    if (deleteAllOfScheduleTag.value != null) {
                        deleteAllOfScheduleTag.postValue(!deleteAllOfScheduleTag.value!!)
                    } else {
                        deleteAllOfScheduleTag.postValue(true)
                    }
                }
            }
        }
    }
}