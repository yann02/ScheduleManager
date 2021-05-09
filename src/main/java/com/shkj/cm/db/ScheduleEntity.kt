package com.shkj.cm.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程管理本地数据库实体类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/20 14:59
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
data class ScheduleEntity(
    val body: Body,
    val code: Int,
    val msg: String
)

@Entity(tableName = "schedule_entity")
data class Body(
    var createtime: String,
    var dtag: Int,
    var endTime: String,
    var freq: Int,
    var gmt: String,
    var monoId: String,
    var startTime: String,
    @PrimaryKey
    var tid: String,
    var title: String
) {
    @Ignore
    lateinit var preTime: List<String>
}

@Entity
data class FrequencyEntity(
    var tid: String,
    var beforeTime: String,
    var eventId: Long = -1
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}