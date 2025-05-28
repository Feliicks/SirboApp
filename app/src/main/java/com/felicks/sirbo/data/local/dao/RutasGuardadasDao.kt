package com.felicks.sirbo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicks.sirbo.data.local.entity.RutaGuardadaEntity

@Dao
interface RutaGuardadaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ruta: RutaGuardadaEntity)

    @Query("SELECT * FROM rutas_guardadas ORDER BY fechaGuardado DESC LIMIT 5")
    suspend fun obtenerUltimasCinco(): List<RutaGuardadaEntity>
}
