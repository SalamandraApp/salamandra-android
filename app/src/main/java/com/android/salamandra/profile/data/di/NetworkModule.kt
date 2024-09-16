package com.android.salamandra.profile.data.di

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.profile.data.RepositoryImpl
import com.android.salamandra.profile.domain.Repository
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
        dataStoreRepository: DataStoreRepository,
        localDbRepository: LocalDbRepository,
        retrofitExceptionHandler: RetrofitExceptionHandler,
        salamandraApiService: SalamandraApiService
    ): Repository {
       return RepositoryImpl(dataStoreRepository, localDbRepository, retrofitExceptionHandler, salamandraApiService)
    }
}