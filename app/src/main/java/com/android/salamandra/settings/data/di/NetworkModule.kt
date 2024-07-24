package com.android.salamandra.settings.data.di

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.settings.data.RepositoryImpl
import com.android.salamandra.settings.domain.Repository
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
        localDbRepository: LocalDbRepository
    ): Repository{
       return RepositoryImpl(cognitoService, localDbRepository)
    }
}