package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.turbine.test
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra.SalamandraLocalDB.Companion.Schema
import com.android.salamandra._core.data.adapter.BooleanAdapter
import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra._core.data.adapter.IntAdapter
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import user.UserEntity
import workout.WorkoutTemplateElementEntity
import workout.WorkoutTemplateEntity
import java.time.LocalDate
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutTemplateDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: WorkoutTemplateDataSource
    private lateinit var db: SalamandraLocalDB

    private val id = "123"
    private val name = "Push up"
    private val description = "This is a dummy description"
    private val dateCreated = LocalDate.parse("2024-02-02")
    private val onlyPreviewAvailable = false
    private val expectedWorkoutTemplateEntity =
        WorkoutTemplateEntity(id, name, description, dateCreated, onlyPreviewAvailable)

    @Before
    fun setUp() {
        val dateAdapter = DateAdapter()
        val intAdapter = IntAdapter()
        val driver = AndroidSqliteDriver(
            Schema,
            InstrumentationRegistry.getInstrumentation().targetContext,
            "test.db"
        )
        db = SalamandraLocalDB(
            driver,
            UserEntityAdapter = UserEntity.Adapter(
                dateAdapter,
                dateAdapter,
                intAdapter,
                intAdapter,
                intAdapter,
                intAdapter
            ),
            WorkoutTemplateEntityAdapter = WorkoutTemplateEntity.Adapter(DateAdapter(), BooleanAdapter()),
            WorkoutTemplateElementEntityAdapter = WorkoutTemplateElementEntity.Adapter(intAdapter, intAdapter, intAdapter)
        )
        dataSource = WorkoutTemplateDataSource(db, testDispatcher)
    }

    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Act
        val insertion =
            dataSource.insertWk(id, name, description, dateCreated, onlyPreviewAvailable)
        runCurrent()
        val wk = dataSource.getWkByID(id)
        runCurrent()

        // Assert
        assert(wk is Result.Success && wk.data == expectedWorkoutTemplateEntity)
        dataSource.clearDatabase()
    }

    @Test
    fun whenInsertingWKTheFlowIsUpdated() = runTest {
        val wkFlow = dataSource.getAllWkPreviews()

        wkFlow.test {
            // Initial state should be empty
            var list = awaitItem()
            assert(list.isEmpty())

            // Insert a new item and ensure the list updates
            dataSource.insertWk(id, name, description, dateCreated, onlyPreviewAvailable)
            runCurrent()
            list = awaitItem()
            //assert(list.size == 1 && list[0] == expectedWorkoutTemplateEntity)

            // Clear the database and ensure the list updates
            dataSource.clearDatabase()
            runCurrent()
            list = awaitItem()
            assert(list.isEmpty())

            // Ensure no more events are emitted
            cancelAndIgnoreRemainingEvents()
        }
    }


}
