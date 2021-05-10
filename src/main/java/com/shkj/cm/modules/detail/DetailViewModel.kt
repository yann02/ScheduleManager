package com.shkj.cm.modules.detail

import androidx.lifecycle.MutableLiveData
import com.shkj.cm.base.viewmodel.BaseViewModel
import com.shkj.cm.common.initiateRequestNotState
import com.shkj.cm.modules.detail.entity.result.ScheduleByIdResultEntity

class DetailViewModel : BaseViewModel<DetailRepository>() {


    val remoteSuccess = MutableLiveData<ScheduleByIdResultEntity>()
    fun remoteSchedulesById(tId: String) {
        initiateRequestNotState {
            val httpBaseBean = mRepository.remoteSchedulesById(tId)
            if (httpBaseBean.code == 8000) {
                remoteSuccess.postValue(httpBaseBean)
            }
        }
    }
}