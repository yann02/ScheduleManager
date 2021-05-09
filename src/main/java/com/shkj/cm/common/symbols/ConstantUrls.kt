package com.shkj.cm.common.symbols

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 管理接口URL地址
 * @Author: Yingyan Wu
 * @CreateDate: 2021/1/21 17:16
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
object ConstantUrls {

    /**
     * 添加日程
     */
    const val URL_ADD_SCHEDULE = "mono-biz-app-meet/user/userScheAddTime"
    const val URL_EDIT_SCHEDULE = "mono-biz-app-meet/user/userScheEditTime"

    /**
     * 获取用户的所有日程(分页)
     */
    const val URL_GET_ALL_OF_SCHEDULE_ON_PAGE = "mono-biz-app-meet/user/getMeetUserScheManagePage"

    /**
     * 删除单个用户日程
     */
    const val URL_DELETE_SCHEDULE = "mono-biz-app-meet/user/delMeetUserScheManage"

    /**
     * 清空所有日程
     */
    const val URL_DELETE_ALL_OF_SCHEDULES = "mono-biz-app-meet/user/delUserScheManage"

    /**
     * 获取用户某日，所有日程
     */
    const val URL_GET_SCHEDULES_ON_DAY = "mono-biz-app-meet/user/getUserSechDayList"

    /**
     * 获取某月内，用户所有日程管理的日期集合
     */
    const val URL_GET_DAYS_BY_MONTH = "mono-biz-app-meet/user/getUserSechDayByMonth"

    /**
     * 获取某月内，用户所有日程管理的日期集合
     */
    const val URL_GET_SCHEDULE_BY_ID = "mono-biz-app-meet/user/getMeetUserScheManageInfo"
}