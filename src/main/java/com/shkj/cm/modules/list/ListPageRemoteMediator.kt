package com.shkj.cm.modules.list

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shkj.cm.db.RemoteKeys
import com.shkj.cm.db.RoomHelper
import com.shkj.cm.modules.list.entities.result.Data
import retrofit2.HttpException
import java.io.IOException

private const val SCHEDULE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class ListPageRemoteMediator(private val list: ListRepository, private val monoId: String) : RemoteMediator<Int, Data>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Data>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: SCHEDULE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }
        try {
            val response = list.getMeetUserScheManagePage(curPage = page, monoId = monoId, pageSize = state.config.pageSize)
            val jobs = response.body.datas
            val endOfPaginationReached = jobs.isEmpty()
            RoomHelper.appDatabase?.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    RoomHelper.remoteKeysDao?.clearRemoteKeys()
                    RoomHelper.listPageDao?.clearAll()
                }
                val prevKey = if (page == SCHEDULE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = jobs.map {
                    RemoteKeys(tId = it.tid, prevKey = prevKey, nextKey = nextKey)
                }
                RoomHelper.remoteKeysDao?.insertAll(keys)
                RoomHelper.listPageDao?.insertAll(jobs)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Data>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { schedule ->
                // Get the remote keys of the last item retrieved
                RoomHelper.remoteKeysDao?.remoteKeysTId(schedule.tid)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Data>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { schedule ->
                // Get the remote keys of the first items retrieved
                RoomHelper.remoteKeysDao?.remoteKeysTId(schedule.tid)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Data>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.tid?.let { id ->
                RoomHelper.remoteKeysDao?.remoteKeysTId(id)
            }
        }
    }
}