package com.shkj.cm

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/16 23:09
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class RequestEntityOfList(
    val body: Body
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class Body(
    val curPage: String,
    val monoId: String,
    val pageSize: String
)