package com.felicks.sirbo.data.local.dao.patternDetailDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.entity.patternDetail.StopEntity

@Dao
interface StopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<StopEntity>)

    @Query("SELECT * FROM stops WHERE patternId = :patternId")
    suspend fun getStopsByPatternId(patternId: String): List<StopEntity>
}
