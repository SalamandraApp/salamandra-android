package com.android.salamandra.domain

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
}