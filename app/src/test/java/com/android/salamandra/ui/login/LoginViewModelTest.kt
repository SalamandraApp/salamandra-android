package com.android.salamandra.ui.login

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.DataError
import com.android.salamandra.domain.error.PasswordError
import com.android.salamandra.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    @RelaxedMockK
    private lateinit var mockkRepository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(mockkRepository, ioDispatcher = StandardTestDispatcher())
    }


    @Test
    fun `When Error, state changed`() = runTest {
        //Arrange
        val expectedError = PasswordError.NO_UPPERCASE

        //Act
        loginViewModel.dispatch(LoginIntent.Error(PasswordError.NO_UPPERCASE))
        runCurrent()
        //Assert
        assert(loginViewModel.state.value.error == expectedError)
    }

    @Test
    fun `When user is authenticated correctly, success is true`() = runTest {
        //Arrange
        val expectedState = LoginState.initial.copy(success = true)
        coEvery { mockkRepository.login(any(), any()) } returns Result.Success(Unit)
        //Act
        loginViewModel.dispatch(LoginIntent.Login)
        advanceUntilIdle()

        //Assert
        assert(loginViewModel.state.value == expectedState)
    }

    @Test
    fun `When user error occurs while authenticationg user, error displayed`() = runTest {
        //Arrange
        coEvery { mockkRepository.login(any(), any()) } returns Result.Error(DataError.Cognito.INVALID_EMAIL_OR_PASSWORD)
        //Act
        loginViewModel.dispatch(LoginIntent.Login)
        runCurrent()

        //Assert
        assert(loginViewModel.state.value.error != null)
    }

}














