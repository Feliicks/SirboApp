package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey val key: String, // ej: "base_url"
    val value: String
)
