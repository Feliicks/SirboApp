package com.felicks.sirbo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.felicks.sirbo.data.local.dao.AppConfigDao
import com.felicks.sirbo.data.local.dao.PatternDao
import com.felicks.sirbo.data.local.dao.PatternGeometryDao
import com.felicks.sirbo.data.local.dao.PlaceDao
import com.felicks.sirbo.data.local.dao.RutaGuardadaDao
import com.felicks.sirbo.data.local.dao.RutasDao
import com.felicks.sirbo.data.local.dao.UserDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.PatternDetailDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.StopDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.TripDao
import com.felicks.sirbo.data.local.entity.AppConfigEntity
import com.felicks.sirbo.data.local.entity.PatternEntity
import com.felicks.sirbo.data.local.entity.PatternGeometryEntity
import com.felicks.sirbo.data.local.entity.PlaceEntity
import com.felicks.sirbo.data.local.entity.RutaEntity
import com.felicks.sirbo.data.local.entity.RutaGuardadaEntity
import com.felicks.sirbo.data.local.entity.UserEntity
import com.felicks.sirbo.data.local.entity.patternDetail.PatternDetailEntity
import com.felicks.sirbo.data.local.entity.patternDetail.StopEntity
import com.felicks.sirbo.data.local.entity.patternDetail.TripEntity

@Database(
    entities = [
        PlaceEntity::class,
        RutaGuardadaEntity::class,
        RutaEntity::class,
        PatternEntity::class,
        PatternDetailEntity::class,
        PatternGeometryEntity::class,
        StopEntity::class,
        TripEntity::class,
        UserEntity::class,
        AppConfigEntity::class,
    ],
    version = 6
)
abstract class SirboDatabase : RoomDatabase() {
    abstract fun placedao(): PlaceDao
    abstract fun rutasGuardadas(): RutaGuardadaDao
    abstract fun rutasDao(): RutasDao
    abstract fun patternDao(): PatternDao
    abstract fun patternDetailDao(): PatternDetailDao
    abstract fun patternGeometryDao(): PatternGeometryDao
    abstract fun stopDao(): StopDao
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao
    abstract fun appConfigdao(): AppConfigDao


//    companion object {
//        @Volatile
//        private var INSTANCE: SirboDatabase? = null
//
//        fun getInstance(context: Context): SirboDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    SirboDatabase::class.java,
//                    DBConstants.DB_NAME
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}





