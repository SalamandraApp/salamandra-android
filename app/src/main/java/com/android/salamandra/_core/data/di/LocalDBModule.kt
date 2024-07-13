package com.android.salamandra._core.data.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.adapter.BooleanAdapter
import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra._core.data.LocalDbRepositoryImpl
import com.android.salamandra._core.data.adapter.IntAdapter
import com.android.salamandra._core.data.sqlDelight.user.UserDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.domain.LocalDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import user.UserEntity
import workout.WorkoutTemplateEntity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    @Singleton
    @Provides
    fun provideSQLDriver(app: Application): SqlDriver = AndroidSqliteDriver(
        SalamandraLocalDB.Schema,
        app,
        "Salamandra local DB"
    )

    @Singleton
    @Provides
    fun provideSQLDelightDB(driver: SqlDriver, dateAdapter: DateAdapter, booleanAdapter: BooleanAdapter, intAdapter: IntAdapter): SalamandraLocalDB {
        return SalamandraLocalDB(
            driver = driver,
            WorkoutTemplateEntityAdapter = WorkoutTemplateEntity.Adapter(
                dateCreatedAdapter = dateAdapter,
                onlyPreviewAvailableAdapter = booleanAdapter
            ),
            UserEntityAdapter = UserEntity.Adapter(
                dateJoinedAdapter = dateAdapter,
                dateOfBirthAdapter = dateAdapter,
                heightAdapter = intAdapter,
                genderAdapter = intAdapter,
                fitnessLevelAdapter = intAdapter,
                fitnessGoalAdapter = intAdapter

            )
        )
    }

    @Singleton
    @Provides
    fun provideLocalDbRepository(
        workoutTemplateDataSource: WorkoutTemplateDataSource,
        userDataSource: UserDataSource
    ): LocalDbRepository {
        return LocalDbRepositoryImpl(workoutTemplateDataSource, userDataSource)
    }

    @Singleton
    @Provides
    fun provideWorkoutTemplateDataSource(
        db: SalamandraLocalDB,
        ioDispatcher: CoroutineDispatcher
    ): WorkoutTemplateDataSource {
        return WorkoutTemplateDataSource(db, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideUserDataSource(
        db: SalamandraLocalDB,
        ioDispatcher: CoroutineDispatcher
    ): UserDataSource {
        return UserDataSource(db, ioDispatcher)
    }

}