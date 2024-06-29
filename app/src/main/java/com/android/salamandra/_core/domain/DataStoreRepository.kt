package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.flow.Flow


interface DataStoreRepository {
    fun getToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
    suspend fun getUidFromDatastore(): Result<String, DataError.Datastore>
    suspend fun saveUid(uid: String)
    suspend fun deleteUid()
}