package com.android.salamandra._core.data.di

import com.android.salamandra._core.data.clock.ClockImpl
import com.android.salamandra._core.domain.clock.Clock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {
    @Singleton
    @Provides
    fun provideClock(): Clock = ClockImpl()
}