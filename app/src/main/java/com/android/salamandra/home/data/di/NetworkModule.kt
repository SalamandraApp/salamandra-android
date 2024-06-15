package com.android.salamandra.home.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra.home.data.RepositoryImpl
import com.android.salamandra.home.domain.Repository
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
    ): Repository{
       return RepositoryImpl(cognitoService)
    }
}