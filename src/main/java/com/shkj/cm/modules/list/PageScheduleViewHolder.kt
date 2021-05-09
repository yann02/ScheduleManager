package com.shkj.cm.modules.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shkj.cm.R
import com.shkj.cm.modules.list.entities.result.Data
import com.xuexiang.xutil.data.DateUtils

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 分页列表的View Holder
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/25 14:23
 * History:
 * @Author: 暂无
 * @Date: 暂无
 * @Description: 暂无
 */
class PageScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.tv_title)
    private val fullDay: TextView = view.findViewById(R.id.tv_space_of_time)
    private val time: TextView = view.findViewById(R.id.tv_space_of_date)
    private val delete: ImageButton = view.findViewById(R.id.ib_for_delete_item)
    private var data: Data? = null
    private var mOnPageListItemClickListener: OnPageListItemClickListener? = null

//    init {
//        view.setOnClickListener {
//            data?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
//        }
//    }

    fun bindClickListener(onPageListItemClickListener: OnPageListItemClickListener) {
        mOnPageListItemClickListener = onPageListItemClickListener
    }

    fun bind(mData: Data?, position: Int) {
        mData?.let {
            showRepoData(it, position)
        }
    }

    private fun showRepoData(mData: Data, position: Int) {
        this.data = mData
        title.text = mData.title
        delete.setOnClickListener {
            mOnPageListItemClickListener?.onItemDeleteClick(position, mData.tid)
        }
        itemView.setOnClickListener {
            mOnPageListItemClickListener?.onItemClick(position, mData.tid)
        }
        if (mData.dtag == 1) {
            //  非全天
            fullDay.visibility = View.GONE
            val startFormatTimeString = DateUtils.millis2String(mData.startTime.toLong(), DateUtils.yyyyMMddHHmm.get())
            val endFormatTimeString = DateUtils.millis2String(mData.endTime.toLong(), DateUtils.yyyyMMddHHmm.get())
            time.text = itemView.context.getString(R.string.page_time_on_item, startFormatTimeString, endFormatTimeString)
        } else {
            //  2全天
            fullDay.visibility = View.VISIBLE
            val startFormatTimeString = DateUtils.millis2String(mData.startTime.toLong(), DateUtils.yyyyMMdd.get())
            val endFormatTimeString = DateUtils.millis2String(mData.endTime.toLong(), DateUtils.yyyyMMdd.get())
            time.text = itemView.context.getString(R.string.page_time_on_item, startFormatTimeString, endFormatTimeString)
        }
    }

    companion object {
        fun create(parent: ViewGroup): PageScheduleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_schedule_all_of_item, parent, false)
            return PageScheduleViewHolder(view)
        }
    }

    interface OnPageListItemClickListener {
        fun onItemClick(position: Int, tid: String)
        fun onItemDeleteClick(position: Int, tid: String)
    }
}
