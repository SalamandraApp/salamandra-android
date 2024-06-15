package com.android.salamandra._core.presentation

import com.android.salamandra.R
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.GenericError
import com.android.salamandra._core.domain.error.PasswordError
import com.android.salamandra._core.domain.error.RootError

fun PasswordError.asUiText(): UiText {
    return when (this) {
        PasswordError.TOO_SHORT -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
        PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
        PasswordError.NO_DIGIT -> UiText.StringResource(R.string.don_t_have_an_account_register) //example TOCHANGE
    }
}

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Cognito.INVALID_EMAIL_OR_PASSWORD -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.SESSION_FETCH -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Network.REQUEST_TIEMOUT -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Network.UNKNOW -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.UNKNOWN_ERROR -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.SIGN_UP_FIELDS_NOT_VALID -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.WRONG_CONFIRMATION_CODE -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.SIGN_OUT_FAILED_USER_SIGNED_IN -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.SIGN_OUT_FAILED_USER_NOT_SIGNED_IN -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
    }
}

fun GenericError.asUiText(): UiText {
    return when (this) {
        GenericError.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error)
    }
}

fun RootError.asUiText(): UiText = when (this) {
    is DataError -> this.asUiText()
    is GenericError -> this.asUiText()
    is PasswordError -> this.asUiText()
}
