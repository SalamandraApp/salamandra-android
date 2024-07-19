package com.android.salamandra.authentication.login.data

import android.util.Log
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.authentication.login.domain.Repository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    @RelaxedMockK
    private lateinit var cognitoService: CognitoService

    @RelaxedMockK
    private lateinit var retrofitExceptionHandler: RetrofitExceptionHandler


    @RelaxedMockK
    private lateinit var loginRepository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginRepository = RepositoryImpl(cognitoService,salamandraApiService, retrofitExceptionHandler, localDbRepository)
    }

    @Test
    fun `When api throws exception, it is handled and error returned`() = runTest{
        // Arrange
        coEvery { cognitoService.login(any(), any()) } returns Result.Success("")
        coEvery { salamandraApiService.getUserData(any()) } throws Exception()
        coEvery { retrofitExceptionHandler.handleException(any()) } returns DataError.Network.UNKNOWN

        // Act
        val loginResult = loginRepository.login("", "")

        // Assert
        assert(loginResult is Result.Error && loginResult.error == DataError.Network.UNKNOWN)
    }


}