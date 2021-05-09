package com.shkj.cm.modules.list.entities.result

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 一键清空接口请求返回的实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 16:10
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DeleteAllOfSchedulesForResultEntity(
    val body: BodyDeleteAllOfSchedulesForResultEntity,
    val code: Int,
    val msg: String
)

data class BodyDeleteAllOfSchedulesForResultEntity(
    val op: Int
)