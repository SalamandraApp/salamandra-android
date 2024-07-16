package com.android.salamandra.workouts.searchExercise.data.di

import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra.workouts.searchExercise.data.RepositoryImpl
import com.android.salamandra.workouts.searchExercise.domain.Repository
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
    fun provideRepository(
        salamandraApiService: SalamandraApiService
    ): Repository {
       return RepositoryImpl(salamandraApiService)
    }
}