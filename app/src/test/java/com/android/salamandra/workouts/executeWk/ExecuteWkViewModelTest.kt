package com.android.salamandra.workouts.executeWk

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXECUTION_EXERCISE
import com.android.salamandra.util.EXAMPLE_EXECUTION_EXERCISES_LIST
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ExecuteWkViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var executeWkViewModel: ExecuteWkViewModel

    @RelaxedMockK
    private lateinit var workoutsRepository: WorkoutsRepository

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        savedStateHandle = mockk(relaxed = true)
        val mockNavArgs = ExecuteWkNavArgs(wkTemplateId = "123")
        every { savedStateHandle.get<String>("wkTemplateId") } returns mockNavArgs.wkTemplateId
        executeWkViewModel = ExecuteWkViewModel(
            ioDispatcher = testDispatcher,
            savedStateHandle = savedStateHandle,
            workoutsRepository = workoutsRepository
        )
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = ExecuteWkState(
            error = null,
            exerciseList = emptyList(),
            currentExercise = null,
            currentSet = 1,
            workoutEnded = false,
            survey = null,
            startOfSetCurrentTimeMillis = 0
        )
        assert(ExecuteWkState.initial == expectedState)
    }

    @Test
    fun `Initial flow of creating a workout execution works`() = runTest {
        // Arrange
        val expectedExecutionExerciseList = EXAMPLE_EXECUTION_EXERCISES_LIST
        val expectedInitialExercise = EXAMPLE_EXECUTION_EXERCISE
        coEvery { workoutsRepository.getWkTemplate(any()) } returns Result.Success(
            EXAMPLE_WORKOUT_TEMPLATE
        )

        // Act
        executeWkViewModel = ExecuteWkViewModel(
            testDispatcher,
            savedStateHandle,
            workoutsRepository
        )
        runCurrent()

        // Assert
        assert(executeWkViewModel.state.value.exerciseList == expectedExecutionExerciseList && executeWkViewModel.state.value.currentExercise == expectedInitialExercise)
    }
}











