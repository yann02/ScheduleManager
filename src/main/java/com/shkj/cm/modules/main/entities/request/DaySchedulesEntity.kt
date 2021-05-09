package com.shkj.cm.modules.main.entities.request

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: remote 当天所有日程的请求实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 8:57
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DaySchedulesEntity(
    val body: Body
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class Body(
    val monoId: String,
    val sechStartTime: String
)