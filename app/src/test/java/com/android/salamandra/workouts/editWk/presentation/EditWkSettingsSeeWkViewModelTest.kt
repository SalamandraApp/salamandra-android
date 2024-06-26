package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra._core.util.EXERCISE
import com.android.salamandra._core.util.WORKOUT_TEMPLATE_ELEMENT
import com.android.salamandra.authentication.login.presentation.LoginViewModel
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.workouts.editWk.domain.Repository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp

@OptIn(ExperimentalCoroutinesApi::class)
class EditWkSettingsSeeWkViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var editWkViewModel: EditWkViewModel

    @RelaxedMockK
    private lateinit var mockkRepository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        editWkViewModel =
            EditWkViewModel(repository = mockkRepository, ioDispatcher = testDispatcher)
    }

    @Test
    fun `Assert initial state`() {
        //Arrange
        val initialState = EditWkState(
            loading = false,
            error = null,
            wkTemplate = WorkoutTemplate(),
            showSearchExercise = false,
            searchTerm = "",
            exerciseList = emptyList()
        )
        //Assert
        assert(EditWkState.initial == initialState)
    }

    @Test
    fun `Error intent and CloseError update the state`() = runTest{
        //Arrange
        var expectedState = EditWkState.initial.copy(error = DataError.Network.UNKNOWN)
        //Act
        editWkViewModel.dispatch(EditWkIntent.Error(error =  DataError.Network.UNKNOWN))
        runCurrent()

        //Assert
        assert(expectedState == editWkViewModel.state.value)

        //Arrange
        expectedState = EditWkState.initial.copy(error = null)
        //Act
        editWkViewModel.dispatch(EditWkIntent.CloseError)
        runCurrent()

        //Assert
        assert(expectedState == editWkViewModel.state.value)
    }

    @Test
    fun `Change description working`() = runTest{
        //Arrange
        val dummyDesc = "Dummy desc"
        val expectedState = EditWkState.initial.wkTemplate.copy(description = dummyDesc)
        //Act
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription(dummyDesc))
        runCurrent()
        //Assert
        assert(expectedState == editWkViewModel.state.value.wkTemplate)
    }

    @Test
    fun `When empty description sent, value is set to null`() = runTest{
        //Arrange
        val dummyDesc = "Dummy desc"
        val expectedState = EditWkState.initial.wkTemplate.copy(description = null)
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription(dummyDesc))
        //Act
        editWkViewModel.dispatch(EditWkIntent.ChangeWkDescription(""))
        runCurrent()
        //Assert
        assert(expectedState == editWkViewModel.state.value.wkTemplate)
    }

    @Test
    fun `When adding multiple exercises, positions are set correctly`() = runTest {
        //Arrange
        val resultList = listOf(
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 1),
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 2),
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 3),
            WORKOUT_TEMPLATE_ELEMENT.copy(exercise = EXERCISE.copy(name = "Squat"), position = 4),
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 5),
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 6),
            WORKOUT_TEMPLATE_ELEMENT.copy(position = 7)
        )
        val expectedState = EditWkState.initial.copy(wkTemplate = WorkoutTemplate(elements = resultList))

        //Act
        for (i in 0..2) {
            editWkViewModel.dispatch(EditWkIntent.AddExerciseToTemplate(EXERCISE))
            runCurrent()
        }
        editWkViewModel.dispatch(EditWkIntent.AddExerciseToTemplate(EXERCISE.copy(name = "Squat")))
        runCurrent()
        for (i in 0..2) {
            editWkViewModel.dispatch(EditWkIntent.AddExerciseToTemplate(EXERCISE))
            runCurrent()
        }
        //Assert
        assert(expectedState == editWkViewModel.state.value)
    }
}