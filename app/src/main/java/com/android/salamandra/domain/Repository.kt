package com.android.salamandra.domain

interface Repository {
    suspend fun login(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit)

    suspend fun register(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit)
}