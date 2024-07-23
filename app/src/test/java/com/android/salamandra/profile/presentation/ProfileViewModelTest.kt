package com.android.salamandra.profile.presentation

import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.home.domain.Repository
import com.android.salamandra.home.presentation.HomeViewModel
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest{
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var profileViewModel: ProfileViewModel


    @RelaxedMockK
    private lateinit var coreRepository: CoreRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { coreRepository.isUserLogged() } returns true
        profileViewModel = ProfileViewModel(testDispatcher, coreRepository)
    }

    @Test
    fun `Assert initial state`(){
        val expectedState =  ProfileState(
            loading = false,
            error = null,
            userData = null,
            isSignedIn = false
        )

        assert(expectedState == ProfileState.initial)
    }

    @Test
    fun `When user is not logged in, state is initial`() = runTest {
        // Arrange
        val expectedState =  ProfileState(
            loading = false,
            error = null,
            userData = null,
            isSignedIn = false
        )
        coEvery { coreRepository.isUserLogged() } returns false
        runCurrent()

        // Act
        profileViewModel = ProfileViewModel(testDispatcher, coreRepository)

        // Assert
        assert(profileViewModel.state.value == expectedState)

    }

    @Test
    fun `When user data is not available, state is initial`() = runTest {
        // Arrange
        coEvery { coreRepository.getUserData() } returns Result.Error(DataError.Datastore.UID_NOT_FOUND)

        // Act
        profileViewModel = ProfileViewModel(testDispatcher, coreRepository)
        runCurrent()

        // Assert
        assert(profileViewModel.state.value.error != null && !profileViewModel.state.value.isSignedIn)

    }


}











