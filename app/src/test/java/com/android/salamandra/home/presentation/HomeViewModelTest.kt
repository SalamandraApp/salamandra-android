package com.android.salamandra.home.presentation

import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.home.domain.Repository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var homeViewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var coreRepository: CoreRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        homeViewModel = HomeViewModel(repository, coreRepository, testDispatcher)
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = HomeState(
            error = null,
            wkPreviewList = emptyList()
        )
        assert(HomeState.initial == expectedState)
    }

    @Test
    fun `When user logged and local empty, remote call is done`() = runTest {
        // Arrange
        coEvery { coreRepository.isUserLogged() } returns true
        coEvery { repository.isLocalDbEmpty() } returns true
        coEvery { repository.getWkPreviewsFromRemoteAndStoreInLocal() } returns Result.Success(Unit)
        coEvery { repository.getWkPreviews() } returns flowOf(emptyList())

        //Act
        homeViewModel = HomeViewModel(repository, coreRepository, testDispatcher)
        runCurrent()

        // Assert
        coVerify(exactly = 1) { repository.getWkPreviewsFromRemoteAndStoreInLocal() }
    }

    @Test
    fun `When user logged and local NOT empty, remote call is NOT done`() = runTest {
        // Arrange
        coEvery { coreRepository.isUserLogged() } returns true
        coEvery { repository.isLocalDbEmpty() } returns false
        coEvery { repository.getWkPreviewsFromRemoteAndStoreInLocal() } returns Result.Success(Unit)
        coEvery { repository.getWkPreviews() } returns flowOf(emptyList())

        //Act
        homeViewModel = HomeViewModel(repository, coreRepository, testDispatcher)
        runCurrent()

        // Assert
        coVerify(exactly = 0) { repository.getWkPreviewsFromRemoteAndStoreInLocal() }
    }

    @Test
    fun `When user NOT logged and local empty, remote call is NOT done`() = runTest {
        // Arrange
        coEvery { coreRepository.isUserLogged() } returns false
        coEvery { repository.isLocalDbEmpty() } returns true
        coEvery { repository.getWkPreviewsFromRemoteAndStoreInLocal() } returns Result.Success(Unit)
        coEvery { repository.getWkPreviews() } returns flowOf(emptyList())

        //Act
        homeViewModel = HomeViewModel(repository, coreRepository, testDispatcher)
        runCurrent()

        // Assert
        coVerify(exactly = 0) { repository.getWkPreviewsFromRemoteAndStoreInLocal() }
    }

    @Test
    fun `When user NOT logged and local NOT empty, remote call is NOT done`() = runTest {
        // Arrange
        coEvery { coreRepository.isUserLogged() } returns false
        coEvery { repository.isLocalDbEmpty() } returns false
        coEvery { repository.getWkPreviewsFromRemoteAndStoreInLocal() } returns Result.Success(Unit)
        coEvery { repository.getWkPreviews() } returns flowOf(emptyList())

        //Act
        homeViewModel = HomeViewModel(repository, coreRepository, testDispatcher)
        runCurrent()

        // Assert
        coVerify(exactly = 0) { repository.getWkPreviewsFromRemoteAndStoreInLocal() }
    }

}