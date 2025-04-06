package com.felicks.sirbo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felicks.sirbo.data.local.dao.PlaceDao
import com.felicks.sirbo.data.local.entity.PlaceEntity

@Database(
    entities = [PlaceEntity::class],
    version = 1
)
abstract  class SirboDatabase : RoomDatabase() {
    abstract fun placedao(): PlaceDao


}





