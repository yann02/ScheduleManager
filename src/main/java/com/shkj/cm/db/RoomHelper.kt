package com.shkj.cm.db

import com.dosmono.platecommon.util.UIUtils

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: ROOM数据库帮助类
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/14 15:20
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
object RoomHelper {
    val appDatabase by lazy {
        AppDataBase.getInstance(UIUtils.getContext())
    }
    val scheduleDao by lazy {
        appDatabase?.scheduleDao()
    }

    /**
     * 分页日程列表对象
     */
    val listPageDao by lazy {
        appDatabase?.listPageDao()
    }

    /**
     * 分页日程列表远程keysDao
     */
    val remoteKeysDao by lazy {
        appDatabase?.remoteKeysDao()
    }
}