package com.shkj.cm.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonParseException
import com.shkj.cm.common.state.State
import com.shkj.cm.common.state.StateType
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/4/16 20:50
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
object NetExceptionHandle {
    fun handleException(e: Throwable?, loadState: MutableLiveData<State>){
        e?.let {
            when (it) {
                is HttpException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is ConnectException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is ConnectTimeoutException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is UnknownHostException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is JsonParseException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is NoClassDefFoundError -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
            }
        }
    }
}