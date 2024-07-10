package com.android.salamandra.authentication.login.data.di


import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.authentication.login.data.RepositoryImpl
import com.android.salamandra.authentication.login.domain.Repository
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
        retrofitExceptionHandler: RetrofitExceptionHandler,
        localDbRepository: LocalDbRepository
    ): Repository {
        return RepositoryImpl(cognitoService, salamandraApiService, retrofitExceptionHandler, localDbRepository)
    }
}