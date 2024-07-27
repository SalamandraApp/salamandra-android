package com.android.salamandra.workouts.seeWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `when local WorkoutTemplate is not found, remote is requested`() = runTest {
        // Arrange

        // Act

        // Assert
    }
}











