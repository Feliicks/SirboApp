package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry

@Entity(tableName = DBConstants.TABLA_GEOMETRIA)
data class PatternGeometryEntity(
    @PrimaryKey val patternId: String,
    val points: String,
    val length: Int
)

// Entidad → Dominio
fun PatternGeometryEntity.toDomain(): PatternGeometry {
    return PatternGeometry(
        points = points,
        length = length
    )
}
// Lista de Entity → Lista de Domain
fun List<PatternGeometryEntity>.toDomainList(): List<PatternGeometry> {
    return this.map { it.toDomain() }
}
