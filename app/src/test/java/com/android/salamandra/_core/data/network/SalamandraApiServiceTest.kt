package com.android.salamandra._core.data.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class SalamandraApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var salamandraApiService: SalamandraApiService

    @Before
    fun createServer() {
        mockWebServer = MockWebServer()

        salamandraApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // setting a dummy url
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SalamandraApiService::class.java)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun `When searchExercise is successful, ExerciseResponse parsed correctly and toDomain works`() = runTest {
        // Arrange
        val response = MockResponse().setBody(jsonSuccessfulSearchExerciseResponse)
        mockWebServer.enqueue(response)
        // Act
        val result = salamandraApiService.searchExercise("")

        // Assert
        assert(result == parsedExerciseResponse && result.toDomain() == toDomainExerciseResponse)
    }


}