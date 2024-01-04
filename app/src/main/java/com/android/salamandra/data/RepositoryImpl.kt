package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.data.network.request.AuthRequest
import com.android.salamandra.data.network.response.AuthResponse
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
) : Repository {

    //Auth
    override suspend fun login(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit){ //Pair: Exit/Failure + msg
        salamandraApiService.login(AuthRequest(username = email, password = password)).enqueue(
            object : Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    onResponse(Pair(false, "Failure during API call to login"))
                }
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    checkResponse(response, onResponse)
                }
            }
        )
    }

    override suspend fun register(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit) {
        salamandraApiService.register(AuthRequest(username = email, password = password)).enqueue(
            object : Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    onResponse(Pair(false, "Failure during API call to register"))
                }
                override fun onResponse( call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    checkResponse(response, onResponse)
                }
            }
        )
    }

    private fun checkResponse(
        response: Response<AuthResponse>,
        onResponse: (Pair<Boolean, String>) -> Unit
    ) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null)
                onResponse(Pair(true, responseBody.token))
            else
                onResponse(Pair(false, "Null response body")) //Code should never get here
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
            onResponse(Pair(false, errorMsg))
        }
    }

    //Ex query
    override suspend fun getExercise(term: String): List<ExerciseModel>? {
        runCatching {
            salamandraApiService.searchExercise(term)
        }
            .onSuccess {
                return it.toDomain()
            }
            .onFailure {
                Log.i("Jaime", "An error ocurred while using apiService, ${it.message}")
            }
        return null
    }
}
