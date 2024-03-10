package com.android.salamandra.domain.usecases.auth

import com.android.salamandra.domain.Repository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: Repository) {
    suspend fun register(email: String, username: String, password: String, onSuccess: () -> Unit) {
        repository.register(
            email = email,
            password = password,
            username = username,
            onSuccess = onSuccess
        )
    }

    suspend fun confirmRegister(username: String, code: String, onSuccess: () -> Unit) {
        repository.confirmRegister(username = username, code = code, onSuccess = { onSuccess() })
    }
}

