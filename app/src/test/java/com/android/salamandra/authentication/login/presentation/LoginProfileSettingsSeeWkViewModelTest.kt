package com.android.salamandra.authentication.login.presentation

import com.android.salamandra.authentication.login.domain.Repository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.PasswordError
import com.android.salamandra._core.domain.error.Result
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
class LoginProfileSettingsSeeWkViewModelTest {
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



