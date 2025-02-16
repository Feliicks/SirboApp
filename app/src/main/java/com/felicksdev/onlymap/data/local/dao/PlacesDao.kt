package com.felicksdev.onlymap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felicksdev.onlymap.data.local.entity.PlaceEntity

@Dao
interface PlaceDao {

    @Query("SELECT * FROM places ORDER BY lastUsedAt DESC LIMIT 5")
    fun getRecentPlaces(): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: PlaceEntity)


    fun updatePlace(place: PlaceEntity) {
//        insertPlace(place)
    }
}