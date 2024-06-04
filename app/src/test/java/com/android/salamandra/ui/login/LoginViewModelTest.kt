package com.android.salamandra.ui.login

import com.android.salamandra.data.RepositoryImpl
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.PasswordError
import com.android.salamandra.ui.asUiText
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest{
    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    @RelaxedMockK
    private lateinit var mockkRepository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(mockkRepository)
    }


    @Test
    fun `When Error, state changed`() = runTest {
        //Arrange
        val expectedError = PasswordError.NO_UPPERCASE

        //Act
        loginViewModel.dispatch(LoginIntent.Error(PasswordError.NO_UPPERCASE.asUiText()))
        runCurrent()
        //Assert
        assert(loginViewModel.state.value.error == expectedError.asUiText())

    }

}