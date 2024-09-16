package com.android.salamandra.workouts.search.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.workouts.editWk.presentation.EditWkNavArgs
import com.android.salamandra.workouts.search.domain.Repository
import io.mockk.MockKAnnotations
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
class SearchViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        savedStateHandle = mockk(relaxed = true)
        val mockNavArgs = SearchNavArgs(
           wkName = "",
            description = null
        )
        every { savedStateHandle.get<String>("wkName") } returns mockNavArgs.wkName
        every { savedStateHandle.get<String>("description") } returns mockNavArgs.description

        searchViewModel = SearchViewModel(testDispatcher, repository, savedStateHandle)
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = SearchState(
            error = null,
            searchTerm = "",
            searchResultExercises = emptyList(),
            addedExercisesIds = emptyList(),
            selectedExercise = null,
            wkName = "",
            description = null
        )
        assert(SearchState.initial == expectedState)
    }

    @Test
    fun `Assert searchTerm changes correctly`() = runTest {
        // Arrange
        val expectedValue = "new search term"

        // Act
        searchViewModel.dispatch(SearchIntent.ChangeSearchTerm(expectedValue))
        runCurrent()

        // Assert
        assert(searchViewModel.state.value.searchTerm == expectedValue)
    }
}