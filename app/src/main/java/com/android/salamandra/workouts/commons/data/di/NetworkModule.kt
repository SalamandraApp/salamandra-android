package com.android.salamandra.workouts.commons.data.di

import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.workouts.commons.data.WorkoutsRepositoryImpl
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideWorkoutsRepository(
        localDbRepository: LocalDbRepository
    ): WorkoutsRepository{
       return WorkoutsRepositoryImpl(localDbRepository)
    }
}