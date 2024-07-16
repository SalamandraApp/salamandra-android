package com.android.salamandra.workouts.editWk.data.di

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.workouts.editWk.data.RepositoryImpl
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
        localDbRepository: LocalDbRepository,
        dataStoreRepository: DataStoreRepository,
        retrofitExceptionHandler: RetrofitExceptionHandler,
        salamandraApiService: SalamandraApiService
    ): com.android.salamandra.workouts.editWk.domain.Repository {
       return RepositoryImpl(localDbRepository = localDbRepository, dataStoreRepository = dataStoreRepository, retrofitExceptionHandler = retrofitExceptionHandler, salamandraApiService = salamandraApiService)
    }
}