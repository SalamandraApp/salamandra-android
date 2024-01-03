package com.android.salamandra.domain.usecases

import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.model.UserModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String, onResponse: (Pair<Boolean, String>) -> Unit) {
       repository.login(email, password){
           onResponse(it)
       }
    }
}