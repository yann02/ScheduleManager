package com.shkj.cm.modules.list.entities.request

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 删除单个日程请求类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 11:29
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DeleteScheduleForRequestEntity(
    val body: BodyDeleteScheduleForRequestEntity
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class BodyDeleteScheduleForRequestEntity(
    val tid: String
)