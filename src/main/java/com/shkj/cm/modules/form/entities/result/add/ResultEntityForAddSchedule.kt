package com.shkj.cm.modules.form.entities.result.add

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/21 16:49
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ResultEntityForAddSchedule(
    val body: Body,
    val code: Int,
    val msg: String
)

data class Body(
    val createtime: String,
    val dtag: Int,
    val endTime: String,
    val freq: Int,
    val gmt: String,
    val monoId: String,
    val preTime: List<String>,
    val startTime: String,
    val statusInfo: Int,
    val tid: String,
    val title: String
)