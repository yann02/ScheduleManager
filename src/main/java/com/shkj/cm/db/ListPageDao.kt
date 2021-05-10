package com.shkj.cm.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shkj.cm.modules.list.entities.result.Data

/**
 * 分页日程列表dao
 */
@Dao
interface ListPageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Data>)

    @Query("SELECT * FROM list_page_entity")
    fun pagingSource(): PagingSource<Int, Data>

    @Query("DELETE FROM list_page_entity")
    suspend fun clearAll()
}