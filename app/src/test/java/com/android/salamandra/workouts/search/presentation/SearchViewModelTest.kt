package com.android.salamandra.workouts.search.presentation

import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.workouts.search.domain.Repository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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

    @RelaxedMockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        searchViewModel = SearchViewModel(testDispatcher, repository)
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = SearchState(
            error = null,
            searchTerm = "",
            searchResultExercises = emptyList(),
            addedExercisesIds = emptyList(),
            selectedExercise = null
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