package com.android.salamandra.domain

import com.android.salamandra.domain.model.UserModel

interface Repository {
    suspend fun login(email: String, password: String): UserModel

    suspend fun register(email: String, password: String): UserModel
}