package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.Pattern

@Entity(tableName = DBConstants.TABLA_PATTERNS)
data class PatternEntity(
    @PrimaryKey val id: String,
    val rutaId: String,      // FK para relacionar patterns con una ruta
    val desc: String
)
// Entidad -> Dominio, rutaId no se usa en dominio
fun PatternEntity.toDomain(): Pattern {
    return Pattern(
        id = this.id,
        desc = this.desc
    )
}
fun List<PatternEntity>.toDomainList(): List<Pattern> = map { it.toDomain() }


