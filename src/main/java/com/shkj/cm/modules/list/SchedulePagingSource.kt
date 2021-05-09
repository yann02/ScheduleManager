package com.shkj.cm.modules.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shkj.cm.modules.list.ListRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/24 16:09
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
const val SCHEDULE_STARTING_PAGE_INDEX = 1

class SchedulePagingSource(private val list: ListRepository, private val monoId: String) : PagingSource<Int, com.shkj.cm.modules.list.entities.result.Data>() {
    override fun getRefreshKey(state: PagingState<Int, com.shkj.cm.modules.list.entities.result.Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.shkj.cm.modules.list.entities.result.Data> {
        val position = params.key ?: SCHEDULE_STARTING_PAGE_INDEX
        return try {
            val response = list.getMeetUserScheManagePage(curPage = position, monoId = monoId, pageSize = params.loadSize)
            val jobs = response.body.datas
            val nextKey = if (jobs.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(data = jobs, prevKey = if (position == SCHEDULE_STARTING_PAGE_INDEX) null else position - 1, nextKey = nextKey)
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}