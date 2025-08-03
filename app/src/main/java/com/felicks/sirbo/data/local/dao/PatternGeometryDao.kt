package com.felicks.sirbo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.local.entity.PatternGeometryEntity
import com.felicks.sirbo.data.local.entity.RutaEntity

@Dao
interface PatternGeometryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(geometry: PatternGeometryEntity)

    @Query("SELECT * FROM ${DBConstants.TABLA_GEOMETRIA} WHERE patternId = :patternId LIMIT 1")
    suspend fun getByPatternId(patternId: String): PatternGeometryEntity?

    @Query("DELETE FROM ${DBConstants.TABLA_GEOMETRIA}")
    suspend fun clearAll()

    // Inserta o reemplaza una lista de rutas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rutas: List<RutaEntity>): List<Long>
}
