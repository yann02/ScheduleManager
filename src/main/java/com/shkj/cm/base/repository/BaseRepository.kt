package com.shkj.cm.base.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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
open class BaseRepository {
    private val mCompositeDisposable by lazy { CompositeDisposable() }

    fun addSubscribe(disposable: Disposable) = mCompositeDisposable.add(disposable)

    fun unSubscribe() = mCompositeDisposable.dispose()
}