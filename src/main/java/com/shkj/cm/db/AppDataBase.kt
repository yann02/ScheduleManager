package com.shkj.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.modules.list.entities.result.Data

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/1/27 14:18
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@Database(entities = [Body::class, FrequencyEntity::class, Data::class, RemoteKeys::class], version = 4)
abstract class AppDataBase : RoomDatabase() {
    /**
     * 日程DAO
     */
    abstract fun scheduleDao(): ScheduleDao

    /**
     * 分页日程列表DAO
     */
    abstract fun listPageDao(): ListPageDao

    /**
     * 分页日程列表远程keysDao
     */
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private var INSTANCE: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            AppDataBase::class.java,
                            UIUtils.getContext().packageName
                        )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE FrequencyEntity " + " ADD COLUMN eventId INTEGER  " + " NOT NULL DEFAULT 0")
            }
        }

        /**
         * 添加分页日程列表实体类
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE `list_page_entity` (`createtime` TEXT, `dtag` INTEGER, `endTime` TEXT" +
                            ", `freq` INTEGER , `gmt` TEXT, `monoId` TEXT, `startTime` , `statusInfo` INTEGER TEXT , `tid` TEXT , `title` TEXT PRIMARY KEY(`tid`))"
                )
            }
        }

        /**
         * 添加分页日程列表实体类
         */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE `remote_keys` (`tId` TEXT, `prevKey` INTEGER, `nextKey` INTEGER" +
                            " PRIMARY KEY(`tId`))"
                )
            }
        }
    }

}