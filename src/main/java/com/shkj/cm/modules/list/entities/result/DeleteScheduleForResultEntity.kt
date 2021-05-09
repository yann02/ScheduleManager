package com.shkj.cm.modules.list.entities.result

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 删除单个日程接口返回类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 11:31
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DeleteScheduleForResultEntity(
    val body: BodyDeleteScheduleForResultEntity,
    val code: Int,
    val msg: String
)

data class BodyDeleteScheduleForResultEntity(
    val op: Int
)