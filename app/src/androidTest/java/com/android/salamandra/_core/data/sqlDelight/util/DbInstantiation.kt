package com.android.salamandra._core.data.sqlDelight.util

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra.SalamandraLocalDB.Companion.Schema
import com.android.salamandra._core.data.adapter.BooleanAdapter
import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra._core.data.adapter.IntAdapter
import exercise.ExerciseEntity
import user.UserEntity
import workout.WorkoutTemplateElementEntity
import workout.WorkoutTemplateEntity

object DbInstantiation {

    fun instantiateTestDB(): SalamandraLocalDB {
        val dateAdapter = DateAdapter()
        val intAdapter = IntAdapter()

        val driver = AndroidSqliteDriver(
            Schema,
            InstrumentationRegistry.getInstrumentation().targetContext,
            "test.db"
        )

        return SalamandraLocalDB(

            driver,
            UserEntityAdapter = UserEntity.Adapter(
                dateAdapter,
                dateAdapter,
                intAdapter,
                intAdapter,
                intAdapter,
                intAdapter
            ),
            WorkoutTemplateEntityAdapter = WorkoutTemplateEntity.Adapter(
                DateAdapter(),
                BooleanAdapter()
            ),
            WorkoutTemplateElementEntityAdapter = WorkoutTemplateElementEntity.Adapter(
                intAdapter,
                intAdapter,
                intAdapter,
                intAdapter
            ),
            ExerciseEntityAdapter = ExerciseEntity.Adapter(
                intAdapter,
                intAdapter,
                intAdapter,
                intAdapter
            )
        )
    }
}