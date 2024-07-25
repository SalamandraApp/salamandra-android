package com.android.salamandra.workouts.editWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_EXERCISE_PUSH_UP_ENTITY
import com.android.salamandra.util.EXAMPLE_EXERCISE_SQUAT_ENTITY
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_SQUAT
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
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
class EditWkRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    @RelaxedMockK
    private lateinit var retrofitExceptionHandler: RetrofitExceptionHandler

    @RelaxedMockK
    private lateinit var dataStoreRepository: DataStoreRepository

    @RelaxedMockK
    private lateinit var log: Log

    private lateinit var repository: RepositoryImpl

    companion object {
        private const val TEMPORARY_SAVED_ELEMENTS_ID = "TEMPORAL"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RepositoryImpl(
            localDbRepository = localDbRepository,
            salamandraApiService = salamandraApiService,
            dataStoreRepository = dataStoreRepository,
            retrofitExceptionHandler = retrofitExceptionHandler
        )
    }

    @Test
    fun `test retrieving temporary elements`() = runTest {
        //Arrange
        val expectedList =
            listOf(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP, EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT)
        coEvery { localDbRepository.getWkTemplateElementsById(TEMPORARY_SAVED_ELEMENTS_ID) } returns Result.Success(
            listOf(
                EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_PUSH_UP,
                EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_SQUAT
            )
        )
        coEvery { localDbRepository.getExerciseByID(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_PUSH_UP.exerciseId) } returns Result.Success(
            EXAMPLE_EXERCISE_PUSH_UP_ENTITY
        )
        coEvery { localDbRepository.getExerciseByID(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_SQUAT.exerciseId) } returns Result.Success(
            EXAMPLE_EXERCISE_SQUAT_ENTITY
        )
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0

        //Act
        val elementList = repository.retrieveSavedWorkoutTemplateElements()
        runCurrent()

        //Assert
        assert(expectedList == elementList)

    }
}