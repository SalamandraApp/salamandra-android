package com.android.salamandra.data.di

import android.content.Context
import com.amplifyframework.core.Amplify
import com.android.salamandra.BuildConfig.BASE_URL
import com.android.salamandra.data.DataStoreRepositoryImpl
import com.android.salamandra.data.RepositoryImpl
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.data.network.interceptor.AuthInterceptor
import com.android.salamandra.domain.DataStoreRepository
import com.android.salamandra.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideDispatcher() = Dispatchers.IO


    @Singleton
    @Provides
    fun provideRepository(
        salamandraApiService: SalamandraApiService,
        cognitoService: CognitoService
    ): Repository {
        return RepositoryImpl(salamandraApiService, cognitoService)
    }

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

    //Datastore
    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(app)

}