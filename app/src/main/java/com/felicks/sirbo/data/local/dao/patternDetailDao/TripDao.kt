package com.felicks.sirbo.data.local.dao.patternDetailDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.entity.patternDetail.TripEntity

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<TripEntity>)

    @Query("SELECT * FROM trips WHERE patternId = :patternId")
    suspend fun getTripsByPatternId(patternId: String): List<TripEntity>
}