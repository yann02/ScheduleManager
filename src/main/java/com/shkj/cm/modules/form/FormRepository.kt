package com.shkj.cm.modules.form

import androidx.lifecycle.MutableLiveData
import com.shkj.cm.base.models.HttpBaseBean
import com.shkj.cm.base.repository.ApiRepository
import com.shkj.cm.common.state.State
import com.shkj.cm.modules.form.entities.request.add.Body
import com.shkj.cm.modules.form.entities.request.add.EditBody
import com.shkj.cm.modules.form.entities.request.add.RequestEntityForAddSchedule
import com.shkj.cm.modules.form.entities.request.add.RequestEntityForEditSchedule
import com.shkj.cm.modules.form.entities.result.add.ResultEntityForAddSchedule
import com.shkj.cm.modules.form.entities.result.add.ResultEntityForEditSchedule

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 表单
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/21 16:21
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class FormRepository(var loadState: MutableLiveData<State>) : ApiRepository() {
    /**
     * 添加日程
     * @param dtag 标记( 1：非全天   2：全天)
     * @param endTime 结束时间(13位时间戳)
     * @param freq 重复( 0，不重复；1，每天；2，每周；3，每月；4，每年)
     * @param gmt (时区)
     * @param monoId (用户ID)
     * @param preTime (频次)
     * @param startTime 开始时间(13位时间戳)
     * @param title 标题
     */
    suspend fun userScheAddTime(
        dtag: String,
        endTime: String,
        freq: String,
        gmt: String,
        monoId: String,
        preTime: List<String>,
        startTime: String,
        title: String
    ): ResultEntityForAddSchedule {
        return apiService.userScheAddTime(
            RequestEntityForAddSchedule(
                Body(
                    dtag,
                    endTime,
                    freq,
                    gmt,
                    monoId,
                    preTime,
                    startTime,
                    title
                )
            )
        )
    }


    /**
     * 编辑日程
     * @param dtag 标记( 1：非全天   2：全天)
     * @param endTime 结束时间(13位时间戳)
     * @param freq 重复( 0，不重复；1，每天；2，每周；3，每月；4，每年)
     * @param gmt (时区)
     * @param monoId (用户ID)
     * @param preTime (频次)
     * @param startTime 开始时间(13位时间戳)
     * @param title 标题
     */
    suspend fun userScheEditTime(
        tid: String,
        dtag: String,
        endTime: String,
        freq: String,
        gmt: String,
        monoId: String,
        preTime: List<String>,
        startTime: String,
        title: String

    ): ResultEntityForEditSchedule {
        return apiService.userScheEditTime(
            RequestEntityForEditSchedule(
                EditBody(
                    dtag,
                    endTime,
                    freq,
                    gmt,
                    monoId,
                    preTime,
                    startTime,
                    title,
                    tid
                )
            )
        )
    }
}