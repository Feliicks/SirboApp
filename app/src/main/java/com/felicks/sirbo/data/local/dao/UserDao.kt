package com.felicks.sirbo.data.local.dao

import androidx.room.*
import com.felicks.sirbo.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clear()

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
