package com.felicks.sirbo.data.models.otpModels.routes

import com.felicks.sirbo.data.local.entity.RutaEntity

data class RutasItem(
    var id: String = "",
    var agencyName: String = "",
    var longName: String = "",
    var mode: String = "",
    var shortName: String = ""
)

fun RutasItem.toEntity(): RutaEntity {
    return RutaEntity(
        id = this.id,
        agencyName = this.agencyName,
        longName = this.longName,
        mode = this.mode,
        shortName = this.shortName
    )
}


fun List<RutasItem>.toEntityList(): List<RutaEntity> {
    return this.map { it.toEntity() }
}
