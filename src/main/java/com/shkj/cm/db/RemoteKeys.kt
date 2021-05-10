package com.shkj.cm.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val tId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
