package com.shkj.cm.modules.main.entities.result

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 某月内，用户所有日程管理的日期集合(接口返回实体类)
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 8:52
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DaysByTheMonthResultEntity(
    val body: List<BodyOnDaysByTheMonthEntity>,
    val code: Int,
    val msg: String
)

data class BodyOnDaysByTheMonthEntity(
    val endTime: String
)