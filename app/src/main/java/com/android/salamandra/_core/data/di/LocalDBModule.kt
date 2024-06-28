package com.android.salamandra._core.data.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.BooleanAdapter
import com.android.salamandra._core.data.sqlDelight.DateAdapter
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import workout.WorkoutTemplateEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
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
    fun provideSQLDelightDB(driver: SqlDriver, dateAdapter: DateAdapter, booleanAdapter: BooleanAdapter): SalamandraLocalDB {
        return SalamandraLocalDB(
            driver = driver,
            WorkoutTemplateEntityAdapter = WorkoutTemplateEntity.Adapter(
                dateCreatedAdapter = dateAdapter,
                onlyPreviewAvailableAdapter = booleanAdapter
            )
        )
    }


    @Singleton
    @Provides
    fun workoutTemplateDataSource(
        database: SalamandraLocalDB,
        ioDispatcher: CoroutineDispatcher
    ): WorkoutTemplateDataSource {
        return WorkoutTemplateDataSourceImpl(database, ioDispatcher)
    }

}