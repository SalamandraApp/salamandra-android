package com.android.salamandra._core.data.sqlDelight.exercise

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXERCISE_PUSH_UP
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExerciseDataSourceTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var exerciseDataSource: ExerciseDataSource
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        exerciseDataSource = ExerciseDataSource(db, testDispatcher)

    }

    @After
    fun clearDatabase() = runTest {
        exerciseDataSource.clearDatabase()
    }

    @Test
    fun testGettingAWk() = runTest {
        // Arrange
        exerciseDataSource.insertExercise(EXAMPLE_EXERCISE_PUSH_UP)

        // Act
        val successResult = exerciseDataSource.getExerciseByID(EXAMPLE_EXERCISE_PUSH_UP.exId)
        val failureResult = exerciseDataSource.getExerciseByID("dummy")

        // Assert
        assert(successResult is Result.Success && successResult.data.toExercise() == EXAMPLE_EXERCISE_PUSH_UP)

        assert(failureResult is Result.Error && failureResult.error == DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)
    }

    @Test
    fun testClearExerciseDb() = runTest {
        // Arrange
        exerciseDataSource.insertExercise(EXAMPLE_EXERCISE_PUSH_UP)
        exerciseDataSource.insertExercise(EXAMPLE_EXERCISE_PUSH_UP.copy(exId = "0"))
        exerciseDataSource.insertExercise(EXAMPLE_EXERCISE_PUSH_UP.copy(exId = "1"))
        exerciseDataSource.insertExercise(EXAMPLE_EXERCISE_PUSH_UP.copy(exId = "2"))

        // Act
        val firstExerciseCount = exerciseDataSource.countElements().toInt()
        exerciseDataSource.clearDatabase()
        val secondExerciseCount = exerciseDataSource.countElements().toInt()

        // Assert
        assert(firstExerciseCount == 4 && secondExerciseCount == 0)

    }

}