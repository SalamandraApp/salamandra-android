package com.android.salamandra.authentication.verifyAccount.data.di

import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra.authentication.verifyAccount.data.RepositoryImpl
import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
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
        cognitoService: CognitoService,
        salamandraApiService: SalamandraApiService,
        dateAdapter: DateAdapter,
        retrofitExceptionHandler: RetrofitExceptionHandler
    ): Repository {
        return RepositoryImpl(cognitoService, salamandraApiService,dateAdapter, retrofitExceptionHandler)
    }
}