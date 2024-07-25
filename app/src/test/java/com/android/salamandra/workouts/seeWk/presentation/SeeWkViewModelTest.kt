package com.android.salamandra.workouts.seeWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXERCISE_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import com.android.salamandra.workouts.editWk.presentation.EditWkNavArgs
import com.android.salamandra.workouts.seeWk.domain.Repository
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
class SeeWkViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var seeWkViewModel: SeeWkViewModel

    @RelaxedMockK
    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var workoutsRepository: WorkoutsRepository

    private lateinit var savedStateHandle: SavedStateHandle


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        savedStateHandle = mockk(relaxed = true)
        val mockNavArgs = SeeWkNavArgs(wkTemplateId = "")
        every { savedStateHandle.get<String>("wkTemplateId") } returns mockNavArgs.wkTemplateId

        seeWkViewModel =
            SeeWkViewModel(testDispatcher, savedStateHandle, repository, workoutsRepository)
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = SeeWkState(
            error = null,
            wkTemplate = WorkoutTemplate(),
            selectedElementIndex = null
        )
        assert(SeeWkState.initial == expectedState)
    }

    @Test
    fun `When WkTemplate is successfully retrieved, state changed`() = runTest {
        // Arrange
        val expectedValue = EXAMPLE_WORKOUT_TEMPLATE
        coEvery { repository.getWkTemplate(any()) } returns Result.Success(EXAMPLE_WORKOUT_TEMPLATE)

        // Act
        seeWkViewModel =
            SeeWkViewModel(testDispatcher, savedStateHandle, repository, workoutsRepository)
        runCurrent()

        // Assert
        assert(seeWkViewModel.state.value.wkTemplate == expectedValue)
    }
}

















