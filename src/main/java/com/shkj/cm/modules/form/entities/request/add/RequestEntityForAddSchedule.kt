package com.shkj.cm.modules.form.entities.request.add

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 添加日程的请求实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/21 16:16
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class RequestEntityForAddSchedule(
    val body: Body
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class Body(
    val dtag: String,
    val endTime: String,
    val freq: String,
    val gmt: String,
    val monoId: String,
    val preTime: List<String>,
    val startTime: String,
    val title: String
)