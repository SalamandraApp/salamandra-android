package com.android.salamandra.domain

import com.android.salamandra.domain.error.PasswordError
import com.android.salamandra.domain.error.Result
import javax.inject.Inject

class UserDataValidator @Inject constructor() {
    fun validatePassword(password: String): Result<Unit, PasswordError>{
        if(password.length < 9)
            return Result.Error(PasswordError.TOO_SHORT)

        if(!password.any{it.isUpperCase()})
            return Result.Error(PasswordError.NO_UPPERCASE)

        if(password.any{it.isDigit()})
            return Result.Error(PasswordError.NO_DIGIT)

        return Result.Success(Unit)
    }

}