package com.android.salamandra.workouts.editWk.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra.workouts.editWk.data.RepositoryImpl
import com.android.salamandra.workouts.editWk.domain.Repository
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
    ): Repository{
       return RepositoryImpl(salamandraApiService)
    }
}