package com.android.salamandra.home.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra.home.data.RepositoryImpl
import com.android.salamandra.home.domain.Repository
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
        workoutTemplateDataSource: WorkoutTemplateDataSource,
        salamandraApiService: SalamandraApiService
    ): Repository{
       return RepositoryImpl(workoutTemplateDataSource, salamandraApiService)
    }
}