package com.felicks.sirbo.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.felicks.sirbo.data.local.dao.PlaceDao
import com.felicks.sirbo.data.local.dao.RutaGuardadaDao
import com.felicks.sirbo.data.local.entity.PlaceEntity
import com.felicks.sirbo.data.local.entity.RutaGuardadaEntity

@Database(
    entities = [PlaceEntity::class, RutaGuardadaEntity::class],
    version = 1
)
abstract class SirboDatabase : RoomDatabase() {
    abstract fun placedao(): PlaceDao
    abstract fun rutasGuardadas(): RutaGuardadaDao

    companion object {
        @Volatile
        private var INSTANCE: SirboDatabase? = null

        fun getInstance(context: Context): SirboDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SirboDatabase::class.java,
                    "SirboApp.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}





