package com.shkj.cm.common

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shkj.cm.base.repository.BaseRepository
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.state.State
import com.shkj.cm.network.NetExceptionHandle
import kotlinx.coroutines.launch

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:26
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
fun <T : BaseRepository> BaseViewModel<T>.initiateRequest(
    block: suspend () -> Unit,
    loadState: MutableLiveData<State>
) {
    viewModelScope.launch {
        runCatching {
            block()
            Log.d("wyy", "success")
        }.onSuccess {
            Log.d("wyy", "success1")
        }.onFailure {
            Log.d("wyy", "fail")
            NetExceptionHandle.handleException(it, loadState)
        }
    }
}

/**
 * 携程网络请求，适用于页面不需要返回状态页面
 * 使用场景：Activity将页面状态交给fragment处理
 */
fun <T : BaseRepository> BaseViewModel<T>.initiateRequestNotState(
    block: suspend () -> Unit
) {
    viewModelScope.launch {
        runCatching {
            block()
//            Log.d("wyy", "runCatching")
        }.onSuccess {
//            Log.d("wyy", "onSuccess it:$it")
        }.onFailure {
            it.printStackTrace()
            Log.d("wyy", "fail:${it.message}")
        }
    }
}