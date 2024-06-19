package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.authentication.login.presentation.LoginViewModel
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.workouts.editWk.domain.Repository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp

class EditWkViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var editWkViewModel: EditWkViewModel

    @RelaxedMockK
    private lateinit var mockkRepository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        editWkViewModel =
            EditWkViewModel(repository = mockkRepository, ioDispatcher = StandardTestDispatcher())
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
    fun `Error intent and CloseError update the state`(){
        //Arrange
        var expectedState = EditWkState.initial.copy(error = DataError.Network.UNKNOW)
        //Act
        editWkViewModel.dispatch(EditWkIntent.Error(error =  DataError.Network.UNKNOW))
        //Assert
        assert(expectedState == editWkViewModel.state.value)

        //Arrange
        expectedState = EditWkState.initial.copy(error = null)
        //Act
        editWkViewModel.dispatch(EditWkIntent.CloseError)

        //Assert
        assert(expectedState == editWkViewModel.state.value)
    }

}