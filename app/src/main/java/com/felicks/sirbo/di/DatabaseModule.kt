package com.felicks.sirbo.di

import android.content.Context
import androidx.room.Room
import com.felicks.sirbo.data.local.dao.RutaGuardadaDao
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
            "SirboApp.db"
        ).build()
    }

    @Provides
    fun provideRutaGuardadaDao(db: SirboDatabase): RutaGuardadaDao {
        return db.rutasGuardadas()
    }
}

