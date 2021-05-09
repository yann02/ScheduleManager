package com.shkj.cm.modules.detail.entity.result

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: remote 当天所有日程接口返回的实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 9:03
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ScheduleByIdResultEntity(
    val body: Body,
    val code: Int,
    val msg: String
)

data class Body(
    val title: String,
    val gmt:String,
    val dtag: Int,
    val startTime: String,
    val endTime: String,
    val freq:String,
    val statusInfo: Int,
    val tid: String,
    val preTime:List<String>,
    val createtime:String
)