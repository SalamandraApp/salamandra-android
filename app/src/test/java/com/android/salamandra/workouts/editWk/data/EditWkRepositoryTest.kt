package com.android.salamandra.workouts.editWk.data

import android.util.Log
import com.android.salamandra._core.data.network.RetrofitExceptionHandler
import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.workouts.editWk.domain.Repository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EditWkRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    @RelaxedMockK
    private lateinit var localDbRepository: LocalDbRepository

    @RelaxedMockK
    private lateinit var salamandraApiService: SalamandraApiService

    @RelaxedMockK
    private lateinit var dataStoreRepository: DataStoreRepository

    private var retrofitExceptionHandler = RetrofitExceptionHandler()

    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var log: Log


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        repository = RepositoryImpl(
            localDbRepository,
            salamandraApiService,
            dataStoreRepository,
            retrofitExceptionHandler
        )
    }
}










