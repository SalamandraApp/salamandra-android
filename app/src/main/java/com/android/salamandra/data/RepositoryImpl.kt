package com.android.salamandra.data

import android.util.Log
import com.android.salamandra.data.cognito.CognitoService
import com.android.salamandra.data.network.SalamandraApiService
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.DataError
import com.android.salamandra.domain.error.Result
import com.android.salamandra.domain.model.ExerciseModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val salamandraApiService: SalamandraApiService,
    private val cognitoService: CognitoService
) : Repository {

    //TODO with the new PL implementation, exception shouldn't surpass this layer, they should be caught and returned as a Result

    //TODO IDEA: why not save the token in the user model and avoid datastore?

    //Auth
    override suspend fun login(email: String, password: String): Result<Unit, DataError.Cognito> {
        logout() //this should be deleted in the future
        return cognitoService.login(email, password)
    }

    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<Unit, DataError.Cognito> =
        cognitoService.register(
            email,
            password,
            username
        )


    override suspend fun confirmRegister(
        username: String,
        code: String,
    ): Result<Unit, DataError.Cognito> = cognitoService.confirmRegister(username, code)


    override suspend fun logout() = cognitoService.logout()

    //Ex query
    override suspend fun getExercise(term: String): List<ExerciseModel>? {
        return emptyList() //TODO
//        runCatching {
//            salamandraApiService.searchExercise(term)
//        }
//            .onSuccess {
//                return it.toDomain()
//            }
//            .onFailure {
//                Log.i("Jaime", "An error ocurred while using apiService, ${it.message}")
//            }
//        return null
    }
}


