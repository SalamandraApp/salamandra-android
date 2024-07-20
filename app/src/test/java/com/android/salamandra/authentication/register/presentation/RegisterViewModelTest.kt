package com.android.salamandra.authentication.register.presentation

import arrow.core.extensions.function0.monad.tailRecM
import com.android.salamandra._core.domain.UserDataValidator
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.PasswordError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.authentication.register.domain.Repository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var registerViewmodel: RegisterViewModel

    @RelaxedMockK
    private lateinit var repository: Repository

    private val userDataValidator: UserDataValidator = UserDataValidator()


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        registerViewmodel = RegisterViewModel(
            repository = repository,
            ioDispatcher = testDispatcher,
            userDataValidator = userDataValidator
        )
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = RegisterState(
            error = null,
            username = "",
            email = "",
            password = "",
            isEmailValid = true,
            passwordFormatError = null
        )
        assert(RegisterState.initial == expectedState)
    }

    @Test
    fun `When register fails, error displayed but passwordFormatError is null`() = runTest {
        // Arrange
        val expectedError = DataError.Cognito.UNKNOWN_ERROR
        coEvery {
            repository.register(
                any(),
                any(),
                any()
            )
        } returns Result.Error(DataError.Cognito.UNKNOWN_ERROR)

        // Act
        registerViewmodel.dispatch(RegisterIntent.OnRegister)
        runCurrent()

        // Assert
        assert(registerViewmodel.state.value.error == expectedError)
    }

    @Test
    fun `When password is not valid, formatError displayed but error null`() = runTest {
        // Arrange
        val expectedError = PasswordError.NO_DIGIT

        // Act
        registerViewmodel.dispatch(RegisterIntent.ChangePassword("QQQQQwerty#"))
        runCurrent()

        // Assert
        assert(registerViewmodel.state.value.passwordFormatError == expectedError && registerViewmodel.state.value.error == null)
    }

    @Test
    fun `When password is valid, formatError is null and error null`() = runTest {
        // Arrange
        val expectedError = null

        // Act
        registerViewmodel.dispatch(RegisterIntent.ChangePassword("1234Qwerty#"))
        runCurrent()

        // Assert
        assert(registerViewmodel.state.value.passwordFormatError == expectedError && registerViewmodel.state.value.error == null)
    }

    @Test
    fun `When email changes but is badly formated, isEmailValid is false`() = runTest {
        // Arrange
        val expectedValue = false

        // Act
        registerViewmodel.dispatch(RegisterIntent.ChangeEmail("j@j@j.com"))
        runCurrent()

        // Assert
        assert(registerViewmodel.state.value.isEmailValid == expectedValue)
    }

    @Test
    fun `When email changes and is correctly formated, isEmailValid is true`() = runTest {
        // Arrange
        val expectedValue = true

        // Act
        registerViewmodel.dispatch(RegisterIntent.ChangeEmail("j@gmail.com"))
        runCurrent()

        // Assert
        assert(registerViewmodel.state.value.isEmailValid == expectedValue)
    }

}













