package com.shkj.cm.common.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

/**
* Copyright (C), 2015-2021, 海南双猴科技有限公司
* @Description: 暂无
* @Author: Yingyan Wu
* @CreateDate: 2021/1/21 10:25
* History:
* @Author: 暂无
* @Date: 暂无
* @Description: 暂无
*/
class Permissions {

    @Volatile
    private var permissionFragment: PermissionFragment? = null

    private fun getInstance(fragmentManager: FragmentManager) =
        permissionFragment ?: synchronized(this) {
            permissionFragment
                ?: if (fragmentManager.findFragmentByTag(TAG) == null) PermissionFragment().run {
                    fragmentManager.beginTransaction().add(this, TAG).commitNow()
                    this
                } else fragmentManager.findFragmentByTag(TAG) as PermissionFragment
        }

    companion object {
        const val TAG = "permissions"
    }

    constructor(activity: AppCompatActivity) {
        permissionFragment = getInstance(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment) {
        permissionFragment = getInstance(fragment.childFragmentManager)
    }

    fun request(vararg permissions: String): MutableLiveData<PermissionResult> {
        return this.requestArray(permissions)
    }

    fun requestArray(permissions: Array<out String>): MutableLiveData<PermissionResult> {
        permissionFragment!!.requestPermissions(permissions)
        return permissionFragment!!.liveData
    }
}