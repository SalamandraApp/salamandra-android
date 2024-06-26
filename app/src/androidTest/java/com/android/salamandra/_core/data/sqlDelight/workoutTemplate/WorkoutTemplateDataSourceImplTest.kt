package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.turbine.test
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra.SalamandraLocalDB.Companion.Schema
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import workouts.workoutTemplates.WorkoutTemplateEntity

//@RunWith(AndroidJUnit5::class)
@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutTemplateDataSourceImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: WorkoutTemplateDataSourceImpl
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        val driver = AndroidSqliteDriver(
            Schema,
            InstrumentationRegistry.getInstrumentation().targetContext,
            "test.db"
        )
        db = SalamandraLocalDB(driver)
        dataSource = WorkoutTemplateDataSourceImpl(db, testDispatcher)
    }
    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Arrange
        val id = "123"
        val name = "Push up"
        val description = "This is a dummy description"
        val expectedWorkoutTemplateEntity = WorkoutTemplateEntity(id, name, description)

        // Act
        val insertion = dataSource.insertWk(id, name, description)
        runCurrent()
        val wk = dataSource.getWkByID(id)
        runCurrent()

        // Assert
        assert(wk is Result.Success && wk.data == expectedWorkoutTemplateEntity)
    }

    @Test
    fun whenInsertingWKTheFlowIsUpdated() = runTest{
        val wkFLow = dataSource.getAllWk()
        val id = "123"
        val name = "Push up"
        val description = "This is a dummy description"
        val expectedWorkoutTemplateEntity = WorkoutTemplateEntity(id, name, description)

        wkFLow.test {
            var list = awaitItem()
            assert(list.isEmpty())
            val insertion = dataSource.insertWk(id, name, description)
            runCurrent()
            list = awaitItem()
            assert(list.size == 1 && list[0] == expectedWorkoutTemplateEntity)
        }

    }
}
