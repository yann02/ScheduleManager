package com.shkj.cm.modules.main.entities.result

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: remote 当天所有日程接口返回的实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 9:03
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DaySchedulesResultEntity(
    val body: List<Body>,
    val code: Int,
    val msg: String
)

data class Body(
    val createTime: String,
    val dtag: Int,
    val endTime: String,
    val scheTid: String,
    val sechStartTime: String,
    val startTime: String,
    val statusInfo: Int,
    val tid: String,
    val title: String
)