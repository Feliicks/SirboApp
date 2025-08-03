package com.felicks.sirbo.data.models.otpModels.routes

import com.felicks.sirbo.data.local.entity.PatternGeometryEntity

data class PatternGeometry(
    val length: Int = 0,
    val points: String = ""
)
// Dominio → Entidad
fun PatternGeometry.toEntity(patternId: String): PatternGeometryEntity {
    return PatternGeometryEntity(
        patternId = patternId,
        points = points,
        length = length
    )
}
// Lista de Domain → Lista de Entity
fun List<PatternGeometry>.toEntityList(patternIds: List<String>): List<PatternGeometryEntity> {
    return this.mapIndexed { index, geometry ->
        geometry.toEntity(patternIds[index])
    }
}
