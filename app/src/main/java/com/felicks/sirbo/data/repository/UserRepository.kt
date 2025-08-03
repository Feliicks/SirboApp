package com.felicks.sirbo.data.repository

import android.content.Context
import com.felicks.sirbo.data.local.dao.UserDao
import com.felicks.sirbo.data.local.entity.UserEntity
import com.felicks.sirbo.data.local.entity.toDomain
import com.felicks.sirbo.domain.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,             // DAO para usuario en Room
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    suspend fun getCurrentUser(): User? {
        val userEntity = userDao.getUser()
        return userEntity?.toDomain()
    }

    suspend fun saveUser(userEntity: UserEntity) {
        userDao.insert(userEntity)
    }

    suspend fun clearUserSession() {
        userDao.deleteAll()
        // Opcional: también limpiar deviceId si quieres
    }

    fun getOrCreateDeviceId(context: Context): String {
        val key = "device_id"
        var deviceId = prefs.getString(key, null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString(key, deviceId).apply()
        }
        return deviceId
    }

    suspend fun initGuestUser(deviceId: String): UserEntity {
        val existingGuest = userDao.getUserById(deviceId)
        return if (existingGuest != null) {
            existingGuest
        } else {
            val guestUser = UserEntity(
                id = deviceId,
                username = "Guest",
                isGuest = true,
                code = "guest",
                email = "",
                deviceId = deviceId,
                photoUrl = "",
//                role = "guest"
            )
            userDao.insert(guestUser)
            guestUser
        }
    }
}
