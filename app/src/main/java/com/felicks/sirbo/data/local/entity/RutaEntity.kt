package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem


@Entity(tableName = DBConstants.TABLA_RUTAS)
data class RutaEntity(
    @PrimaryKey var id: String,
    var agencyName: String = "",
    var longName: String = "",
    var mode: String = "",
    var shortName: String = ""
)

fun RutaEntity.toDomain(): RutasItem {
    return RutasItem(
        id = this.id,
        agencyName = this.agencyName,
        longName = this.longName,
        mode = this.mode,
        shortName = this.shortName
    )
}

// Listas: Entidad -> Dominio
fun List<RutaEntity>.toDomainList(): List<RutasItem> = map { it.toDomain() }