package com.android.salamandra.home.data

import com.android.salamandra._core.data.cognito.CognitoService
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra.home.domain.Repository

class RepositoryImpl(private val cognitoService: CognitoService): Repository {
    override suspend fun getExercise(term: String): List<Exercise>? {
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

    override suspend fun logout() = cognitoService.logout()
}