package com.felicks.sirbo.data.models.otpModels

import androidx.annotation.Keep
import com.felicks.sirbo.data.local.entity.PatternEntity

@Keep
data class Pattern(
    val id: String,
    val desc: String
)

// Dominio no tiene rutaId, por eso lo pasamos como parámetro al convertir a entidad
fun Pattern.toEntity(rutaId: String): PatternEntity {
    return PatternEntity(
        id = this.id,
        rutaId = rutaId,
        desc = this.desc
    )
}

// Para convertir lista de dominio a entidad es necesario pasar rutaId
fun List<Pattern>.toEntityList(rutaId: String): List<PatternEntity> = map { it.toEntity(rutaId) }