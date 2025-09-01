package com.felicks.sirbo.core.di

import android.content.Context
import com.felicks.sirbo.data.repository.PlanRepositoryImp
import com.felicks.sirbo.domain.repository.PlanRespository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindPlanRepository(
        impl: PlanRepositoryImp
    ): PlanRespository
}

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}
