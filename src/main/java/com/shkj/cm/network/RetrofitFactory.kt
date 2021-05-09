package com.shkj.cm.network

import com.shkj.cm.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

private val DEFAULT_TIMEOUT: Long = 30

class RetrofitFactory private constructor() {
    private val retrofit : Retrofit

    fun <T> createRetrofit(clazz: Class<T>) : T {
        return retrofit.create(clazz)
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(configClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    companion object {
        val instance by lazy {
            RetrofitFactory()
        }
    }

    private fun configClient(): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) // socket timeout
        okHttpClient.addInterceptor(LoggerInterceptor())
        return okHttpClient.build()
    }
}