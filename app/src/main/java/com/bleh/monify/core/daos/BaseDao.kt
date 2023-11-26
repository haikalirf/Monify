package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BaseDao {
    @Query("DELETE FROM sqlite_sequence")
    suspend fun deleteAllPrimaryKeys()
}