package com.android.salamandra.workouts.commons.data.di

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
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
        salamandraApiService: SalamandraApiService,
        dataStoreRepository: DataStoreRepository,
        retrofitExceptionHandler: RetrofitExceptionHandler,
        localDbRepository: LocalDbRepository
    ): WorkoutsRepository {
        return WorkoutsRepositoryImpl(
            salamandraApiService,
            dataStoreRepository,
            retrofitExceptionHandler,
            localDbRepository
        )
    }
}