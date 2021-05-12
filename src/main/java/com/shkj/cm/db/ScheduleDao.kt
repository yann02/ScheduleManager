package com.shkj.cm.db

import androidx.room.*
import kotlinx.coroutines.selects.select

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 日程管理Dao
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/20 15:28
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@Dao
interface ScheduleDao {
    /**
     * 增（日程）
     */
    @Insert
    fun insertScheduleEntity(entity: Body)

    /**
     * 增（日程对应的频次）
     * 一个日程对应多个频次
     */
    @Insert
    fun insertFrequencyForSchedule(entity: FrequencyEntity)

    /**
     * 批量增（日程对应的频次）
     * 一个日程对应多个频次
     */
    @Insert
    fun insertFrequencyForSchedule(entity: List<FrequencyEntity>)

    @Transaction
    fun insertFrequenciesForSchedule(entity: List<FrequencyEntity>) {
        entity.map {
            insertFrequencyForSchedule(it)
        }
    }


    @Update
    fun updateFrequencyForSchedule(entity: Body)

    /**
     * 新增日程
     */
    @Transaction
    fun insertSchedule(entity: Body) {
        insertScheduleEntity(entity)
//        for (it in entity.preTime) {
//            val frequencyEntity = FrequencyEntity(entity.tid, it)
//            insertFrequencyForSchedule(frequencyEntity)
//        }
    }

    /**
     * 删除一个日程
     */
    @Query("delete from schedule_entity where tid = :tid")
    fun deleteScheduleEntity(tid: String)

    /**
     * 删除一个日程对应的频次（可能是多个频次）
     */
    @Query("delete from FrequencyEntity where tid = :tid")
    fun deleteFrequencyEntitiesOnSchedule(tid: String)

    /**
     * 删除一个日程
     */
    @Transaction
    fun deleteScheduleTransaction(tid: String) {
        deleteScheduleEntity(tid)
        deleteFrequencyEntitiesOnSchedule(tid)
    }

    /**
     * 清空日程
     */
    @Query("delete from schedule_entity")
    fun deleteAllOfScheduleEntities()

    /**
     * 清空日程对应的频次
     */
    @Query("delete from FrequencyEntity")
    fun deleteAllOfFrequencyEntitiesOnSchedule()

    /**
     * 清空日程和频次的数据
     */
    @Transaction
    fun deleteAllOfSchedulesTransaction() {
        deleteAllOfScheduleEntities()
        deleteAllOfFrequencyEntitiesOnSchedule()
    }

    /**
     * 更新日程对应的频次
     */
    @Transaction
    fun updateFrequencyEntitiesOnSchedule(entity: Body) {
        deleteFrequencyEntitiesOnSchedule(entity.tid)
        for (it in entity.preTime) {
            val frequencyEntity = FrequencyEntity(entity.tid, it)
            insertFrequencyForSchedule(frequencyEntity)
        }
    }

    /**
     * 根据日程查询 频次
     */
    @Query("select * from FrequencyEntity where tid= :tid")
    fun queryFrequencyEntitiesOnSchedule(tid: String): List<FrequencyEntity>
}