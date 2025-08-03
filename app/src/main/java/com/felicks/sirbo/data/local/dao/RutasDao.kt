package com.felicks.sirbo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.local.entity.RutaEntity

@Dao
interface RutasDao {

    // Inserta o reemplaza una lista de rutas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rutas: List<RutaEntity>): List<Long>

    // Obtiene todas las rutas
    @Query("SELECT * FROM ${DBConstants.TABLA_RUTAS}")
    suspend fun getAllRoutes(): List<RutaEntity>

    // Obtiene una ruta por su id
    @Query("SELECT * FROM ${DBConstants.TABLA_RUTAS} WHERE id = :id LIMIT 1")
    suspend fun getRouteById(id: String): RutaEntity?

    // Elimina una ruta específica
    @Delete
    suspend fun deleteRoute(ruta: RutaEntity)

    // Elimina todas las rutas
    @Query("DELETE FROM ${DBConstants.TABLA_RUTAS}")
    suspend fun deleteAllRoutes()

    // Actualiza una ruta (puedes usar insert con REPLACE también)
    @Update
    suspend fun updateRoute(ruta: RutaEntity)
}
