package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import app.cash.turbine.test
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXERCISE_PUSH_UP
import com.android.salamandra.util.EXAMPLE_USER
import com.android.salamandra.util.EXAMPLE_WORKOUT_PREVIEW
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ENTITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import workout.WorkoutTemplateEntity
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutTemplateDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: WorkoutTemplateDataSource
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        dataSource = WorkoutTemplateDataSource(db, testDispatcher)
    }

    @After
    fun clearDatabase() = runTest {
        dataSource.clearDatabase()
    }

    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Arrange
        val expectedWorkoutTemplateEntity = EXAMPLE_WORKOUT_TEMPLATE_ENTITY
        // Act
        dataSource.insertWk(EXAMPLE_WORKOUT_TEMPLATE.wkId, EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, EXAMPLE_WORKOUT_TEMPLATE.dateCreated, false)
        runCurrent()
        val wk = dataSource.getWkByID(EXAMPLE_WORKOUT_TEMPLATE.wkId)
        runCurrent()

        // Assert
        assert(wk is Result.Success && wk.data == expectedWorkoutTemplateEntity)
        dataSource.clearDatabase()
    }

    @Test
    fun whenInsertingWKTheFlowIsUpdated() = runTest {
        // Arrange
        val expectedWorkoutTemplateEntity = EXAMPLE_WORKOUT_PREVIEW

        // Act
        val wkFlow = dataSource.getAllWkPreviews()

        wkFlow.test {
            // Initial state should be empty
            var list = awaitItem()
            assert(list.isEmpty())

            // Insert a new item and ensure the list updates
            dataSource.insertWk(EXAMPLE_WORKOUT_TEMPLATE.wkId, EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
            runCurrent()
            list = awaitItem()
            assert(list.size == 1 && list[0] == expectedWorkoutTemplateEntity)

            // Clear the database and ensure the list updates
            dataSource.clearDatabase()
            runCurrent()
            list = awaitItem()
            assert(list.isEmpty())

            // Ensure no more events are emitted
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testClearWorkoutTemplateDb() = runTest {
        // Arrange
        dataSource.insertWk(EXAMPLE_WORKOUT_TEMPLATE.wkId, EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
        dataSource.insertWk("0", EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
        dataSource.insertWk("1", EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
        dataSource.insertWk("2", EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
        dataSource.insertWk("3", EXAMPLE_WORKOUT_TEMPLATE.name, EXAMPLE_WORKOUT_TEMPLATE.description, dateCreated = null, onlyPreviewAvailable = true)
        runCurrent()

        // Act
        val firstExerciseCount = dataSource.countElements().toInt()
        dataSource.deleteWkByID("0")
        val secondExerciseCount = dataSource.countElements().toInt()
        dataSource.clearDatabase()
        val thirdExerciseCount = dataSource.countElements().toInt()
        val isDbEmpty = dataSource.isWkTemplateEntityEmpty()
        runCurrent()

        // Assert
        assert(firstExerciseCount == 5 && secondExerciseCount == 4 && thirdExerciseCount == 0 && isDbEmpty)
    }


}
