package com.android.salamandra._core.presentation

import com.android.salamandra.R
import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.GenericError
import com.android.salamandra._core.domain.error.PasswordError
import com.android.salamandra._core.domain.error.RootError

fun PasswordError.asUiText(): UiText {
    return when (this) {
        PasswordError.TOO_SHORT -> UiText.StringResource(R.string.password_must_contain_at_least_9_characters)
        PasswordError.NO_UPPERCASE -> UiText.StringResource(R.string.password_must_have_uppercase)
        PasswordError.NO_DIGIT -> UiText.StringResource(R.string.password_must_have_digits)
        PasswordError.NO_SPECIAL_CHARACTER -> UiText.StringResource(R.string.password_must_have_special_characters)
    }
}

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Cognito.INVALID_EMAIL_OR_PASSWORD -> UiText.StringResource(R.string.incorrect_email_or_password_account_not_confirmed)
        DataError.Cognito.SESSION_FETCH -> UiText.StringResource(R.string.error_fetching_auth_session)
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.request_timed_out)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.failure_caused_by_too_many_requests)
        DataError.Network.UNKNOWN -> UiText.StringResource(R.string.unknown_network_error)
        DataError.Cognito.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_cognito_error)
        DataError.Cognito.SIGN_UP_FIELDS_NOT_VALID -> UiText.StringResource(R.string.email_or_username_already_in_use)
        DataError.Cognito.WRONG_CONFIRMATION_CODE -> UiText.StringResource(R.string.the_verification_code_is_not_correct)
        DataError.Cognito.SIGN_OUT_FAILED_USER_SIGNED_IN -> UiText.StringResource(R.string.an_error_occurred_user_not_signed_out)
        DataError.Cognito.SIGN_OUT_FAILED_USER_NOT_SIGNED_IN -> UiText.StringResource(R.string.an_error_occured_user_signed_out)
        DataError.Local.WORKOUT_TEMPLATE_NOT_FOUND -> UiText.StringResource(R.string.workout_not_found)
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
