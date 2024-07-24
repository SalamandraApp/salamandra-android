package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.flow.Flow


interface DataStoreRepository {
    suspend fun getUidFromDatastore(): Result<String, DataError.Datastore>
    suspend fun saveUid(uid: String)
    suspend fun deleteUid()
}