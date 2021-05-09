package com.shkj.cm.modules.list

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程实体类（分页）
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/16 22:35
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ScheduleOnPageEntity(
    val body: Body,
    val code: Int,
    val msg: String
)

data class Body(
    val datas: List<Data>,
    val firstResult: Int,
    val lastResult: Int,
    val nextPage: Int,
    val pageNo: Int,
    val pageSize: Int,
    val totalCount: Int,
    val totalPage: Int,
    val upPage: Int
)

data class Data(
    val createtime: String,
    val dtag: Int,
    val endTime: String,
    val freq: Int,
    val gmt: String,
    val monoId: String,
    val startTime: String,
    val statusInfo: Int,
    val tid: String,
    val title: String
)