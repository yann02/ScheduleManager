package com.shkj.cm.modules.detail.entity.request

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 某月内，用户所有日程管理的日期集合(接口请求实体类)
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 8:50
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ScheduleByIdEntity(
    val body: BodyById
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class BodyById(
    val tid: String
)