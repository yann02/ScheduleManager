package com.shkj.cm.modules.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.shkj.cm.modules.list.entities.result.Data

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 分页日程列表的适配器
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 14:25
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
class AdapterOnPageSchedules(onPageListItemClickListener: PageScheduleViewHolder.OnPageListItemClickListener) :
    PagingDataAdapter<Data, PageScheduleViewHolder>(PAGE_ITEM_COMPARATOR) {
    private val mOnPageListItemClickListener = onPageListItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageScheduleViewHolder {
        return PageScheduleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PageScheduleViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindClickListener(mOnPageListItemClickListener)
            holder.bind(item, position)
        }
    }

    companion object {
        private val PAGE_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem.tid == newItem.tid

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem == newItem
        }
    }
}
