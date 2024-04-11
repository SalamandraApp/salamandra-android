package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
    private val cognitoService: CognitoService
) : Repository {

    //TODO with the new PL implementation, exception shouldn't surpass this layer, they should be caught and returned as a Result

    //TODO IDEA: why not save the token in the user model and avoid datastore?

    //Auth
    override suspend fun login(email: String, password: String, onSuccess: () -> Unit) {
        logout()
        cognitoService.login(
            email,
            password,
            onSuccess = {
                onSuccess()
            },
            onError = {
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
                throw Exception(it.message)
            }
        )
    }

    override suspend fun confirmRegister(username: String, code: String, onSuccess: () -> Unit) {
        cognitoService.confirmRegister(
            username,
            code,
            onSuccess = {
                onSuccess()
            },
            onError = {
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


