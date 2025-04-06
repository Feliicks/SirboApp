package com.felicks.sirbo.di

import com.felicks.sirbo.data.repository.PlanRepositoryImp
import com.felicks.sirbo.domain.repository.PlanRespository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindPlanRepository(
        impl: PlanRepositoryImp
    ): PlanRespository // ✅ Conectamos la interfaz con la implementación
}