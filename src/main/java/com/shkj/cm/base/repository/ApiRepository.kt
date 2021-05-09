package com.shkj.cm.base.repository

import com.shkj.cm.network.ApiService
import com.shkj.cm.network.RetrofitFactory

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
abstract class ApiRepository : BaseRepository() {
    val apiService: ApiService by lazy {
        RetrofitFactory.instance.createRetrofit(ApiService::class.java)
    }
}