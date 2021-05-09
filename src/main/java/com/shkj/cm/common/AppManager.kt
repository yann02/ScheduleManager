package com.shkj.cm.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*
import kotlin.system.exitProcess

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:26
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
class AppManager {
    private val activityStack: Stack<Activity> = Stack()

    companion object {
        val instance by lazy {
            AppManager()
        }
    }

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    fun exitApp(context: Context) {
        finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }

    private fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }
}