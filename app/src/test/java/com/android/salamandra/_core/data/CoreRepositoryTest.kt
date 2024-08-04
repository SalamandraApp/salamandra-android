package com.android.salamandra._core.data

import android.util.Log
import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_USER
import com.android.salamandra.util.EXAMPLE_USER_ENTITY
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoreRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    @RelaxedMockK
    private lateinit var dataStoreRepository: DataStoreRepository

    @RelaxedMockK
    private lateinit var cognitoService: CognitoService

    private lateinit var repository: CoreRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        repository = CoreRepositoryImpl(cognitoService, dataStoreRepository, localDbRepository)
    }

    @Test
    fun `When uid is retrieved correctly and it is stored in local, User is returned`() = runTest {
        // Arrange
        coEvery { dataStoreRepository.getUidFromDatastore() } returns Result.Success("123")
        coEvery { localDbRepository.getUserByID("123") } returns Result.Success(EXAMPLE_USER_ENTITY)
        val expectedValue = EXAMPLE_USER

        // Act
        val result = repository.getUserData()
        runCurrent()

        // Assert
        assert(result is Result.Success && result.data == expectedValue)
    }
}