package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
    private val cognitoService: CognitoService
) : Repository {

    //Auth
    override suspend fun login(email: String, password: String, onSuccess: () -> Unit) {
        cognitoService.login(
            email,
            password,
            onSuccess = {
                Log.i("SLM", "Login success")
                onSuccess()
            },
            onError = {
                Log.e("SLM", "An error occurred while using cognitoService, ${it.message}")
                throw Exception(it.message)
            }
        )
    }

    override suspend fun register(
        email: String,
        password: String,
        username: String,
        onSuccess: () -> Unit
    ) {
        cognitoService.register(
            email,
            password,
            username,
            onSuccess = {
                onSuccess()
            },
            onError = {
                Log.e("SLM", "An error ocurred while using cognitoService, ${it.message}")
                throw Exception(it.message)
            }
        )
    }

    override suspend fun confirmRegister(username: String, code: String, onSuccess: () -> Unit) {
        cognitoService.confirmRegister(
            username,
            code,
            onSuccess = {
                Log.i("SLM", "Register confirmed")
                onSuccess()
            },
            onError = {
                Log.e("SLM", "An error ocurred while using cognitoService, ${it.message}")
                throw Exception(it.message)
            })
    }

    override suspend fun logout() = cognitoService.logout()

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


