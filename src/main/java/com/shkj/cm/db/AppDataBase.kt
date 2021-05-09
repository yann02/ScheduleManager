package com.shkj.cm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dosmono.platecommon.util.UIUtils

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/1/27 14:18
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@Database(entities = [Body::class, FrequencyEntity::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    /**
     * 日程DAO
     */
    abstract fun scheduleDao(): ScheduleDao

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
                            .addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE FrequencyEntity " + " ADD COLUMN eventId INTEGER  " + " NOT NULL DEFAULT 0");
            }
        }
    }

}