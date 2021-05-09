package com.shkj.cm.common.callback

import com.kingja.loadsir.callback.Callback
import com.shkj.cm.R

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:24
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
class LoadingCallBack : Callback() {
    override fun onCreateView(): Int = R.layout.layout_loading
}