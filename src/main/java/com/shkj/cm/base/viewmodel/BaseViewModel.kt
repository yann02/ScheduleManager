package com.shkj.cm.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shkj.cm.base.repository.BaseRepository
import com.shkj.cm.common.util.CommonUtil
import com.shkj.cm.common.state.State

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:23
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
open class BaseViewModel<T : BaseRepository> : ViewModel(){
    val loadState by lazy {
        MutableLiveData<State>()
    }

    val mRepository : T by lazy {
        (CommonUtil.getClass<T>(this))
            .getDeclaredConstructor(MutableLiveData::class.java)
            .newInstance(loadState)
    }

    override fun onCleared() {
        super.onCleared()
        mRepository.unSubscribe()
    }
}
