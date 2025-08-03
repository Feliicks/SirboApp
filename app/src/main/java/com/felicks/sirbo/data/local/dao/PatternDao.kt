package com.felicks.sirbo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.local.entity.PatternEntity

@Dao
interface PatternDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patterns: List<PatternEntity>): List<Long>

    @Query("SELECT * FROM ${DBConstants.TABLA_PATTERNS} WHERE id = :patternId LIMIT 1")
    suspend fun getPatternById(patternId: String): PatternEntity?

    @Query("SELECT * FROM ${DBConstants.TABLA_PATTERNS} WHERE rutaId = :rutaId")
    suspend fun getPatternsByRutaId(rutaId: String): List<PatternEntity>

    @Query("SELECT * FROM ${DBConstants.TABLA_PATTERNS}")
    suspend fun getAllPatterns(): List<PatternEntity>

    @Query("DELETE FROM ${DBConstants.TABLA_PATTERNS}")
    suspend fun clearPatterns()
}
