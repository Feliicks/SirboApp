package com.felicks.sirbo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felicks.sirbo.domain.models.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val code: String,
    val username: String,
    val email: String?,
    val photoUrl: String?,
    val isGuest: Boolean,
    val deviceId: String,
)

fun UserEntity.toDomain(): User = User(
    id = id,
    code = code,
    username = username,
    email = email,
    photoUrl = photoUrl,
    isGuest = isGuest,
    deviceId = deviceId,
)

