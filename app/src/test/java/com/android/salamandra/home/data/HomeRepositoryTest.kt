package com.android.salamandra.home.data

import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.data.network.response.WkPreviewsResponse
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.home.domain.Repository
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
class HomeRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    private var retrofitExceptionHandler = RetrofitExceptionHandler()

    @RelaxedMockK
    private lateinit var dataStoreRepository: DataStoreRepository


    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { dataStoreRepository.getUidFromDatastore() } returns Result.Success("123")
        coEvery { salamandraApiService.getWorkoutPreviews(any()) } returns WkPreviewsResponse(
            count = 0,
            items = emptyList()
        )
        coEvery { localDbRepository.insertWkPreviewList(any()) } returns Result.Success(Unit)

        repository = RepositoryImpl(
            localDbRepository,
            salamandraApiService,
            dataStoreRepository,
            retrofitExceptionHandler
        )
    }

    @Test
    fun `when dataStore returns Error, getPreviewsFromRemote fails`() = runTest {
        // Arrange
        coEvery { dataStoreRepository.getUidFromDatastore() } returns Result.Error(DataError.Datastore.UID_NOT_FOUND)

        // Act
        val result = repository.getWkPreviewsFromRemoteAndStoreInLocal()

        // Assert
        assert(result is Result.Error && result.error == DataError.Datastore.UID_NOT_FOUND)
    }

    @Test
    fun `When http call fails, exception is thrown and handled`() = runTest {
        // Arrange
        coEvery { salamandraApiService.getWorkoutPreviews(any()) } throws Exception()

        // Act
        val result = repository.getWkPreviewsFromRemoteAndStoreInLocal()
        runCurrent()

        // Assert
        assert(result is Result.Error)
    }

    @Test
    fun `when local insertion returns Error, getPreviewsFromRemote fails`() = runTest {
        // Arrange
        coEvery { localDbRepository.insertWkPreviewList(any()) } returns Result.Error(DataError.Local.ERROR_INSERTING_WK_TEMPLATES)

        // Act
        val result = repository.getWkPreviewsFromRemoteAndStoreInLocal()
        runCurrent()
        
        // Assert
        assert(result is Result.Error && result.error == DataError.Local.ERROR_INSERTING_WK_TEMPLATES)
    }
}