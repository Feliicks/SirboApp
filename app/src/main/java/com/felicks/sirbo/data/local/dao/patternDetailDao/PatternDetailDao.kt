package com.felicks.sirbo.data.local.dao.patternDetailDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.entity.patternDetail.PatternDetailEntity

@Dao
interface PatternDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pattern: PatternDetailEntity)

    @Query("SELECT * FROM pattern_details WHERE routeId = :routeId")
    suspend fun getByRouteId(routeId: String): List<PatternDetailEntity>

    @Query("SELECT * FROM pattern_details WHERE id = :patternId")
    suspend fun getById(patternId: String): PatternDetailEntity?
}
