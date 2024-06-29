package com.android.salamandra._core.domain.error

sealed interface DataError : RootError {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        UNKNOWN
    }

    enum class Cognito : DataError {
        UNKNOWN_ERROR,
        INVALID_EMAIL_OR_PASSWORD,
        USERNAME_OR_EMAIL_ALREADY_IN_USE,
        SESSION_FETCH,
        WRONG_CONFIRMATION_CODE,
        SIGN_OUT_FAILED_USER_SIGNED_IN,
        SIGN_OUT_FAILED_USER_NOT_SIGNED_IN
    }

    enum class Local: DataError {
        WORKOUT_TEMPLATE_NOT_FOUND,
        ERROR_INSERTING_WK_TEMPLATES
    }

    enum class Datastore: DataError{
        UID_NOT_FOUND
    }
}