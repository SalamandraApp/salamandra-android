package com.android.salamandra.data

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.ExerciseModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
    private val cognitoService: CognitoService
) : Repository {

    //Auth
    override suspend fun login(email: String, password: String) =
        cognitoService.login(email, password)

    override suspend fun register(email: String, password: String, username: String) =
        cognitoService.register(email, password, username)

    override suspend fun confirmRegister(username: String, code: String) =
        cognitoService.confirmRegister(username, code)


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
