package com.shkj.cm.common.state

import androidx.annotation.StringRes

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:25
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
data class State(var code: StateType, var message: String = "", @StringRes var tip: Int = 0)