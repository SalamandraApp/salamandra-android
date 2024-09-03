package com.android.salamandra.workouts.executeWk

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.domain.clock.Clock
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXECUTION_EXERCISE
import com.android.salamandra.util.EXAMPLE_EXECUTION_EXERCISES_LIST
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import com.android.salamandra.workouts.executeWk.domain.Repository
import com.android.salamandra.workouts.executeWk.presentation.ExecuteWkIntent
import com.android.salamandra.workouts.executeWk.presentation.ExecuteWkNavArgs
import com.android.salamandra.workouts.executeWk.presentation.ExecuteWkState
import com.android.salamandra.workouts.executeWk.presentation.ExecuteWkViewModel
import com.android.salamandra.workouts.executeWk.presentation.components.ExecuteWkScreenDestinations
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
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

    @RelaxedMockK
    private lateinit var repository: Repository

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var clock: Clock

    @Before
    fun setUp() = runTest {
        MockKAnnotations.init(this)
        savedStateHandle = mockk(relaxed = true)
        val mockNavArgs = ExecuteWkNavArgs(wkTemplateId = EXAMPLE_WORKOUT_TEMPLATE.wkId)
        every { savedStateHandle.get<String>("wkTemplateId") } returns mockNavArgs.wkTemplateId

        workoutsRepository = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        coEvery {
            workoutsRepository.getWkTemplate(any())
        } returns Result.Success(EXAMPLE_WORKOUT_TEMPLATE)
        clock = mockk(relaxed = true)
        every { clock.currentTimeMillis() } returns 0

        executeWkViewModel = ExecuteWkViewModel(
            ioDispatcher = testDispatcher,
            savedStateHandle = savedStateHandle,
            clock = clock,
            workoutsRepository = workoutsRepository,
            repository = repository
        )
        runCurrent()

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
            startOfSetCurrentTimeMillis = 0,
            selectedElement = null,
            workoutTemplateId = "",
            activeTab = ExecuteWkScreenDestinations.ExecuteScreen
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
            clock,
            workoutsRepository,
            repository
        )
        runCurrent()

        // Assert
        assert(executeWkViewModel.state.value.exerciseList == expectedExecutionExerciseList && executeWkViewModel.state.value.currentExercise == expectedInitialExercise && executeWkViewModel.state.value.workoutTemplateId == EXAMPLE_WORKOUT_TEMPLATE.wkId)
    }

    @Test
    fun `Executing one complete workout works`() = runTest {
        // Arrange
        val state = ExecuteWkState.initial.copy(
            exerciseList = EXAMPLE_EXECUTION_EXERCISES_LIST,
            currentExercise = EXAMPLE_EXECUTION_EXERCISE,
            workoutTemplateId = EXAMPLE_WORKOUT_TEMPLATE.wkId
        )

        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        runCurrent()
        // Assert
        assert(executeWkViewModel.state.value == state.copy(currentSet = 4))

        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        runCurrent()
        // Assert
        assert(
            executeWkViewModel.state.value == state.copy(
                currentSet = 2,
                currentExercise = EXAMPLE_EXECUTION_EXERCISES_LIST[1]
            )
        )

        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        runCurrent()
        // Assert
        assert(
            executeWkViewModel.state.value == state.copy(
                currentSet = 4,
                currentExercise = EXAMPLE_EXECUTION_EXERCISES_LIST[1],
                workoutEnded = true
            )
        )
    }

    @Test
    fun `Skipping different sets work correctly`() = runTest {
        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        runCurrent()


        // Assert
        assert(executeWkViewModel.state.value.workoutEnded)
    }

    @Test
    fun `Deleting one whole exercise work correctly`() = runTest {
        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.LogAction)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        runCurrent()

        // Assert
        assert(executeWkViewModel.state.value.workoutEnded && executeWkViewModel.state.value.exerciseList.size == 1)
    }

    @Test
    fun `Changing weight, reps, and rest works as expected`() = runTest {
        // Arrange

        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.ShowBottomSheet(2))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditReps(18))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditWeight(100.3))
        executeWkViewModel.dispatch(ExecuteWkIntent.ShowBottomSheet(3))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditRest(120))
        runCurrent()

        // Assert
        assert(executeWkViewModel.state.value.exerciseList[0].executionElements[1].reps == 18)
        assert(executeWkViewModel.state.value.exerciseList[0].executionElements[1].weight == 100.3)
        assert(executeWkViewModel.state.value.exerciseList[0].executionElements[2].rest == 120)
    }

    @Test
    fun `Changing weight, reps, rest and sikiping the exercise does not throw exception`() = runTest {
        // Act
        executeWkViewModel.dispatch(ExecuteWkIntent.ShowBottomSheet(2))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditReps(18))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditWeight(100.3))
        executeWkViewModel.dispatch(ExecuteWkIntent.ShowBottomSheet(3))
        executeWkViewModel.dispatch(ExecuteWkIntent.EditRest(120))
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        runCurrent()
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        runCurrent()
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        runCurrent()
        executeWkViewModel.dispatch(ExecuteWkIntent.SkipSet)
        runCurrent()

        // Assert
        assert(executeWkViewModel.state.value.exerciseList.size == 1)
    }


}











