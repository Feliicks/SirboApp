package com.felicks.sirbo.domain.models

import com.felicks.sirbo.data.local.entity.UserEntity

data class User(
    val id: String,                  // UID si está autenticado, UUID local si es invitado
    val code: String,                  // UID si está autenticado, UUID local si es invitado
    val username: String,
    val email: String?,
    val photoUrl: String?,
    val isGuest: Boolean,             // true si no inició sesión
    val deviceId: String  // Nuevo campo para guardar el device ID
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    code = code,
    username = username,
    email = email,
    photoUrl = photoUrl,
    isGuest = isGuest,
    deviceId = deviceId,
)