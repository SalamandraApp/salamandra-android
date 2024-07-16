package com.android.salamandra.workouts.commons.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.workouts.commons.data.WorkoutsRepositoryImpl
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
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
    fun provideWorkoutsRepository(
        localDbRepository: LocalDbRepository
    ): WorkoutsRepository{
       return WorkoutsRepositoryImpl(localDbRepository)
    }
}