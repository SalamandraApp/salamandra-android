package com.android.salamandra.splash.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra.splash.data.RepositoryImpl
import com.android.salamandra.splash.domain.Repository
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
        salamandraApiService: SalamandraApiService,
        dataStoreRepository: DataStoreRepository
    ): Repository {
       return RepositoryImpl(workoutTemplateDataSource, salamandraApiService, dataStoreRepository)
    }
}