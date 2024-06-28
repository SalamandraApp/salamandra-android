package com.android.salamandra._core.domain


interface CoreRepository {
    suspend fun isUserLogged(): Boolean
}