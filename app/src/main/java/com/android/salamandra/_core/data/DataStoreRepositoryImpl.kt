package com.android.salamandra._core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.salamandra._core.domain.DataStoreRepository
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "salamandra_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {

    companion object {
        private val UID = stringPreferencesKey("uid")
    }

    override suspend fun getUidFromDatastore(): Result<String, DataError.Datastore> {
        val uid = context.dataStore.data.first()[UID]
        return if (uid != null) Result.Success(uid)
        else Result.Error(DataError.Datastore.UID_NOT_FOUND)
    }

    override suspend fun saveUid(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[UID] = uid
        }
    }

    override suspend fun deleteUid() {
        context.dataStore.edit { preferences ->
            preferences.remove(UID)
        }
    }
}