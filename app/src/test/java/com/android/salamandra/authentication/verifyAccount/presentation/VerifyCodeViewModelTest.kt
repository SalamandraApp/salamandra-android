package com.android.salamandra.authentication.verifyAccount.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra.navArgs
import com.android.salamandra.util.CoroutineRule
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
class VerifyCodeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var verifyCodeViewModel: VerifyCodeViewModel

    @RelaxedMockK
    private lateinit var repository: Repository

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // Mock SavedStateHandle
        savedStateHandle = mockk(relaxed = true)

        // Mock the NavArgs
        val mockNavArgs = VerifyCodeNavArgs(
            username = "testUser",
            email = "test@example.com",
            password = "password123"
        )

        // Mock the behavior of navArgs() method
        every { savedStateHandle.get<String>("username") } returns mockNavArgs.username
        every { savedStateHandle.get<String>("email") } returns mockNavArgs.email
        every { savedStateHandle.get<String>("password") } returns mockNavArgs.password

        verifyCodeViewModel = VerifyCodeViewModel(
            savedStateHandle = savedStateHandle,
            ioDispatcher = testDispatcher,
            repository = repository
        )
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = VerifyCodeState(
            error = null,
            username = "",
            email = "",
            code = "",
            password = ""
        )
        assert(VerifyCodeState.initial == expectedState)
    }

    @Test
    fun `When verification fails, error shown`() = runTest {
        // Arrange
        val expectedValue = DataError.Cognito.UNKNOWN_ERROR
        coEvery { repository.confirmRegister(any(), any(), any(), any()) } returns Result.Error(
            DataError.Cognito.UNKNOWN_ERROR
        )

        // Act
        verifyCodeViewModel.dispatch(VerifyCodeIntent.ConfirmCode)
        runCurrent()

        // Assert
        assert(verifyCodeViewModel.state.value.error == expectedValue)
    }


}







