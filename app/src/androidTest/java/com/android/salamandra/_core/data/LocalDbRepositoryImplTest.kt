package com.android.salamandra._core.data

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.exercise.ExerciseDataSource
import com.android.salamandra._core.data.sqlDelight.exercise.toExercise
import com.android.salamandra._core.data.sqlDelight.user.UserDataSource
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateElementDataSource
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.toWkTemplateElement
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDbRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var userDataSource: UserDataSource
    private lateinit var exerciseDataSource: ExerciseDataSource
    private lateinit var workoutTemplateElementDataSource: WorkoutTemplateElementDataSource
    private lateinit var workoutTemplateDataSource: WorkoutTemplateDataSource
    private lateinit var db: SalamandraLocalDB
    private lateinit var localDbRepository: LocalDbRepository

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        userDataSource = UserDataSource(db, testDispatcher)
        exerciseDataSource = ExerciseDataSource(db, testDispatcher)
        workoutTemplateElementDataSource = WorkoutTemplateElementDataSource(db, testDispatcher)
        workoutTemplateDataSource = WorkoutTemplateDataSource(db, testDispatcher)

        localDbRepository = LocalDbRepositoryImpl(
            workoutTemplateDataSource = workoutTemplateDataSource,
            workoutTemplateElementDataSource = workoutTemplateElementDataSource,
            exerciseDataSource = exerciseDataSource,
            userDataSource = userDataSource
        )
    }

    @After
    fun clearDatabase() = runTest {
        userDataSource.clearDatabase()
        exerciseDataSource.clearDatabase()
        workoutTemplateDataSource.clearDatabase()
        workoutTemplateElementDataSource.clearDatabase()
    }

    @Test
    fun testBasicInsertAndGetWorkoutTemplate() = runTest {
        //Arrange
        val expectedWkTemplate = EXAMPLE_WORKOUT_TEMPLATE

        //Act
        //Insert
        localDbRepository.insertWkTemplate(expectedWkTemplate)

        //Get
        val resultWkTemplate = localDbRepository.getWkTemplate(EXAMPLE_WORKOUT_TEMPLATE.wkId)

        //Assert
        assert(resultWkTemplate is Result.Success && resultWkTemplate.data == expectedWkTemplate)
    }

    @Test
    fun testInsertingTemporalWkElementsAndRetrievingThem() = runTest {
        //Arrange
        val TEMPORAL = "TEMPORAL"
        val expectedList = listOf(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP, EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT)

        //Act
        localDbRepository.insertWkTemplateElement(
            wkTemplateId = TEMPORAL,
            wkTemplateElement = EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP
        )
        localDbRepository.insertWkTemplateElement(
            wkTemplateId =TEMPORAL,
            wkTemplateElement = EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
        )

        var resultList: List<WkTemplateElement> = emptyList()
        localDbRepository.insertExercise(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP.exercise)
        localDbRepository.insertExercise(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT.exercise)
        when (val wkTemplateElements =
            localDbRepository.getWkTemplateElementsById(TEMPORAL)) {
            is Result.Success -> {
                val wkTemplateElementList = wkTemplateElements.data.mapNotNull {
                    when (val exercise = localDbRepository.getExerciseByID(it.exerciseId)) {
                        is Result.Success -> it.toWkTemplateElement(exercise.data.toExercise())
                        is Result.Error -> {
                            null
                        }
                    }
                }
                localDbRepository.deleteTemplateElementById(TEMPORAL)
                resultList = wkTemplateElementList
            }

            is Result.Error -> {
                emptyList<WkTemplateElement>()
            }

        }
        val newDbData = localDbRepository.getWkTemplateElementsById(TEMPORAL)
        //Assert
        assert(resultList == expectedList && newDbData is Result.Success && newDbData.data.isEmpty())
    }
}