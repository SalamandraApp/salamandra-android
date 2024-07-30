package com.android.salamandra.workouts.seeWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ENTITY
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_RESPONSE
import com.android.salamandra.workouts.seeWk.domain.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SeeWkRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    @RelaxedMockK
    private lateinit var dataStoreRepository: DataStoreRepository

    private var retrofitExceptionHandler = RetrofitExceptionHandler()

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        coEvery { dataStoreRepository.getUidFromDatastore() } returns Result.Success("")
        repository = RepositoryImpl(
            salamandraApiService,
            dataStoreRepository,
            retrofitExceptionHandler,
            localDbRepository
        )

    }

    @Test
    fun `when local WorkoutTemplate is not found, remote is requested and log is shown`() =
        runTest {
            // Arrange
            coEvery { localDbRepository.getWkPreviewByID(any()) } returns Result.Error(DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND)

            coEvery {
                salamandraApiService.getWorkoutById(
                    any(),
                    any(),
                    any()
                )
            } returns EXAMPLE_WORKOUT_TEMPLATE_RESPONSE


            // Act
            repository.getWkTemplate("")

            // Assert
            coVerify(exactly = 1) { salamandraApiService.getWorkoutById(any(), any(), any()) }
            verify(exactly = 1) { Log.e(any(), any()) }
        }

    @Test
    fun `When wk is saved in local, no remote call is made`() = runTest {
        // Arrange
        coEvery { localDbRepository.getWkPreviewByID(any()) } returns Result.Success(EXAMPLE_WORKOUT_TEMPLATE_ENTITY)

        // Act
        repository.getWkTemplate("")

        // Assert
        coVerify(exactly = 0) { salamandraApiService.getWorkoutById(any(), any(), any()) }
    }
}











