package com.android.salamandra.ui

import com.android.salamandra.R
import com.android.salamandra.domain.error.PasswordError
import com.android.salamandra.domain.error.Result

fun PasswordError.asUiText(): UiText {
    return when (this) {
        PasswordError.TOO_SHORT -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
        PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
        PasswordError.NO_DIGIT -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
    }
}

fun Result.Error<*, PasswordError>.asErrorUiText(): UiText {
    return error.asUiText()
}