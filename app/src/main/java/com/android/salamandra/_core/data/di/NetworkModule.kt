package com.android.salamandra._core.data.di

import com.android.salamandra.BuildConfig.BASE_URL
import com.android.salamandra._core.data.CoreRepositoryImpl
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.interceptor.AuthInterceptor
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra.splash.data.RepositoryImpl
import com.android.salamandra.splash.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    fun provideLoginApiService(retrofit: Retrofit): SalamandraApiService {
        return retrofit.create(SalamandraApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideRepository(
        cognitoService: CognitoService
    ): CoreRepository{
        return CoreRepositoryImpl(cognitoService)
    }


}