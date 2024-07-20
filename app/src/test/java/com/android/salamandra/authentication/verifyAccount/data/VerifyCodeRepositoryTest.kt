package com.android.salamandra.authentication.verifyAccount.data

import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VerifyCodeRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    @RelaxedMockK
    private lateinit var cognitoService: CognitoService

    private val retrofitExceptionHandler = RetrofitExceptionHandler()

    private val dateAdapter = DateAdapter()

    @RelaxedMockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RepositoryImpl(
            cognitoService,
            salamandraApiService,
            dateAdapter,
            retrofitExceptionHandler
        )
    }

    @Test
    fun `When network call fails, exception handled`() = runTest {
        // Arrange
        val expectedValue = DataError.Network.UNKNOWN
         coEvery { salamandraApiService.createUser(any()) } throws Exception()
        coEvery { cognitoService.confirmRegister(any(), any(), any(), any()) } returns Result.Success("123")
        // Act
        val result = repository.confirmRegister("", "", "", "")

        // Assert
        assert(result is Result.Error && result.error == expectedValue)
    }

}







