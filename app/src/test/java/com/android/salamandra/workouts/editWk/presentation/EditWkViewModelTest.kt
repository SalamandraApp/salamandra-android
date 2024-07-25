package com.android.salamandra.workouts.editWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXERCISE_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
import com.android.salamandra.workouts.editWk.domain.Repository
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
class EditWkViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var editWkViewModel: EditWkViewModel

    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {

        MockKAnnotations.init(this)

        savedStateHandle = mockk(relaxed = true)
        val mockNavArgs = EditWkNavArgs(
            emptyArray()
        )
        every { savedStateHandle.get<Array<String>>("addedExercises") } returns mockNavArgs.addedExercises

        coEvery { repository.retrieveSavedWorkoutTemplateElements() } returns listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
        )
        coEvery { repository.getAllExercises(any()) } returns listOf(EXAMPLE_EXERCISE_PUSH_UP)

        editWkViewModel =
            EditWkViewModel(
                repository = repository,
                ioDispatcher = testDispatcher,
                savedStateHandle = savedStateHandle
            )
    }

    @Test
    fun `Assert initial state`() {
        //Arrange
        val initialState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
            selectedElementIndex = null
        )
        //Assert
        assert(EditWkState.initial == initialState)
    }

    @Test
    fun `when entering edit from search, all exercises loaded`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT,
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP)
        )


        // Act
        editWkViewModel = EditWkViewModel(testDispatcher, savedStateHandle, repository)
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `when entering edit from home, no exercises loaded`() = runTest {
        // Arrange
        val expectedWkTemplateElements = emptyList<WkTemplateElement>()
        coEvery { repository.retrieveSavedWorkoutTemplateElements() } returns emptyList()
        coEvery { repository.getAllExercises(any()) } returns emptyList()
        // Act
        editWkViewModel = EditWkViewModel(testDispatcher, savedStateHandle, repository)
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `Change description working`() = runTest {
        //Arrange
        val expectedValue = "Dummy desc"
        //Act
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription(expectedValue))
        runCurrent()
        //Assert
        assert(expectedValue == editWkViewModel.state.value.wkTemplate.description)
    }

    @Test
    fun `Change description with empty description keeps it null`() = runTest {
        //Arrange
        val expectedValue = null
        //Act
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription("dummy so that desc is not null"))
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription(""))
        runCurrent()
        //Assert
        assert(expectedValue == editWkViewModel.state.value.wkTemplate.description)
    }

    @Test
    fun `Creating a workout gets the positions correctly`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP.copy(position = 1),
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT.copy(position = 2),
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP, position = 3)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.CreateWorkout)
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements )
    }

    @Test
    fun `Changing reps works`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP.copy(reps = 12),
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT,
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP).copy(reps = 1)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.ChangeReps(newReps = 12, index = 0))
        editWkViewModel.dispatch(EditWkIntent.ChangeReps(newReps = 1, index = 2))
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `Changing sets works`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT.copy(sets = 6),
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.ChangeSets(newSets = 6, index = 1))
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `Changing weight works`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT,
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP).copy(weight = 134.6)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.ChangeWeight(newWeight = 134.6, index = 2))
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `Changing rest works`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT,
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP).copy(rest = 15)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.ChangeRest(newRest = 15, index = 2))
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }

    @Test
    fun `removing element works`() = runTest {
        // Arrange
        val expectedWkTemplateElements = listOf(
            EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP,
            WkTemplateElement(exercise = EXAMPLE_EXERCISE_PUSH_UP)
        )

        // Act
        editWkViewModel.dispatch(EditWkIntent.DeleteWkElement(1))
        runCurrent()

        // Assert
        assert(editWkViewModel.state.value.wkTemplate.elements == expectedWkTemplateElements)
    }


}