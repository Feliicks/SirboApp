package com.felicksdev.onlymap.di

import com.felicksdev.onlymap.data.repository.PlanRepositoryImp
import com.felicksdev.onlymap.domain.repository.PlanRespository
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