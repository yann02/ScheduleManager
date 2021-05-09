package com.shkj.cm.modules.main

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程实体类（某天）
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/16 22:33
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ScheduleOfDayEntity(
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