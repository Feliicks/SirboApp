package com.felicks.sirbo.di

import android.content.Context
import androidx.room.Room
import com.felicks.sirbo.data.local.dao.PatternDao
import com.felicks.sirbo.data.local.dao.PatternGeometryDao
import com.felicks.sirbo.data.local.dao.RutaGuardadaDao
import com.felicks.sirbo.data.local.dao.RutasDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.PatternDetailDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.StopDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.TripDao
import com.felicks.sirbo.data.local.database.DBConstants
import com.felicks.sirbo.data.local.database.SirboDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): SirboDatabase {
        return Room.databaseBuilder(
            appContext,
            SirboDatabase::class.java,
            DBConstants.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRutaGuardadaDao(db: SirboDatabase): RutaGuardadaDao {
        return db.rutasGuardadas()
    }

    @Provides
    fun provideRutasDao(database: SirboDatabase): RutasDao {
        return database.rutasDao()
    }

    @Provides
    fun provideVariantesRutaDao(database: SirboDatabase): PatternDao {
        return database.patternDao()
    }
    @Provides
    fun provideVarianteDetalle(database: SirboDatabase): PatternDetailDao {
        return database.patternDetailDao()
    }

    @Provides fun providePatternGeometryDao(db: SirboDatabase): PatternGeometryDao {
        return db.patternGeometryDao()
    }

    @Provides
    fun provideStopsDao(database: SirboDatabase): StopDao {
        return database.stopDao()
    }

    @Provides
    fun provideTripsDao(database: SirboDatabase): TripDao {
        return database.tripDao()
    }

}

