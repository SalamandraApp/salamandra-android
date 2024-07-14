package com.android.salamandra.workouts.seeWk.data.di

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.workouts.seeWk.data.RepositoryImpl
import com.android.salamandra.workouts.seeWk.domain.Repository
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
        salamandraApiService: SalamandraApiService,
        dataStoreRepository: DataStoreRepository,
        retrofitExceptionHandler: RetrofitExceptionHandler,
        localDbRepository: LocalDbRepository
    ): Repository {
        return RepositoryImpl(
            salamandraApiService,
            dataStoreRepository,
            retrofitExceptionHandler,
            localDbRepository
        )
    }
}