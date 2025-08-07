package com.felicks.sirbo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.entity.AppConfigEntity

@Dao
interface AppConfigDao {
    @Query("SELECT * FROM app_config WHERE key = :key")
    suspend fun getConfig(key: String): AppConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: AppConfigEntity)

    @Query("DELETE FROM app_config WHERE key = :key")
    suspend fun deleteConfig(key: String)
}