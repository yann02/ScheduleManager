package com.shkj.cm.modules.list.entities.request

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 一键清空接口请求的实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/26 16:07
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class DeleteAllOfSchedulesForRequestEntity(
    val body: BodyDeleteAllOfSchedulesForRequestEntity
) {
    val sys = "andriod"
    val sysVer = "1.0.0"
    val ver = "1.0"
}

data class BodyDeleteAllOfSchedulesForRequestEntity(
    val monoId: String
)