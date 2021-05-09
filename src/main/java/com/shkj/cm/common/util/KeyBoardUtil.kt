package com.shkj.cm.common.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

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
object KeyBoardUtil {
    // 关闭软键盘
    fun Fragment.hideKeyboard() {
        // 当前焦点的 View
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}