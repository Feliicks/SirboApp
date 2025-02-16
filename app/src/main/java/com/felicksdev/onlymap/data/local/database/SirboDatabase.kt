package com.felicksdev.onlymap.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felicksdev.onlymap.data.local.dao.PlaceDao
import com.felicksdev.onlymap.data.local.entity.PlaceEntity

@Database(
    entities = [PlaceEntity::class],
    version = 1
)
abstract  class SirboDatabase : RoomDatabase() {
    abstract fun placedao(): PlaceDao


}





