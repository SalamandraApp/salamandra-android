package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.turbine.test
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra.SalamandraLocalDB.Companion.Schema
import com.android.salamandra._core.data.sqlDelight.BooleanAdapter
import com.android.salamandra._core.data.DateAdapter
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import workout.WorkoutTemplateEntity
import java.util.Date

//@RunWith(AndroidJUnit5::class)
@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutTemplateDataSourceImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: WorkoutTemplateDataSourceImpl
    private lateinit var db: SalamandraLocalDB

    private val id = "123"
    private val name = "Push up"
    private val description = "This is a dummy description"
    private val dateCreated = Date(1719567576560)
    private val onlyPreviewAvailable = false
    private val expectedWorkoutTemplateEntity = WorkoutTemplateEntity(id, name, description, dateCreated, onlyPreviewAvailable)

    @Before
    fun setUp() {
        val driver = AndroidSqliteDriver(
            Schema,
            InstrumentationRegistry.getInstrumentation().targetContext,
            "test.db"
        )
        db = SalamandraLocalDB(driver, WorkoutTemplateEntity.Adapter(DateAdapter(), BooleanAdapter()))
        dataSource = WorkoutTemplateDataSourceImpl(db, testDispatcher)
    }

    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Act
        val insertion = dataSource.insertWk(id, name, description, dateCreated, onlyPreviewAvailable)
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
