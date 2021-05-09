package com.shkj.cm.modules.main

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.shkj.cm.R
import com.shkj.cm.databinding.AdapterScheduleDayOfItemBinding
import com.shkj.cm.modules.main.entities.result.Body

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 9:49
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
class AdapterOnDaySchedules(data: MutableList<Body>) : BaseQuickAdapter<Body, BaseViewHolder>(R.layout.adapter_schedule_day_of_item, data) {
//    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
//        DataBindingUtil.bind<AdapterScheduleDayOfItemBinding>(viewHolder.itemView)
//    }

    override fun convert(holder: BaseViewHolder, item: Body) {
        //获取binding
        DataBindingUtil.bind<AdapterScheduleDayOfItemBinding>(holder.itemView)
        val adapter: AdapterScheduleDayOfItemBinding? = DataBindingUtil.getBinding(holder.itemView)
        if (null != adapter) {
            adapter.model = item
            //  根据position设置左侧分割线颜色
            adapter.position = holder.layoutPosition
        }
    }
}