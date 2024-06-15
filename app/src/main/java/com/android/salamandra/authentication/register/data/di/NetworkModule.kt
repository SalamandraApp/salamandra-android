package com.android.salamandra.authentication.register.data.di


import com.android.salamandra.authentication.register.data.RepositoryImpl
import com.android.salamandra.authentication.register.domain.Repository
import com.android.salamandra._core.data.cognito.CognitoService
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
        cognitoService: CognitoService
    ): Repository {
        return RepositoryImpl(cognitoService)
    }
}