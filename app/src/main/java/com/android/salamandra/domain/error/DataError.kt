package com.android.salamandra.domain.error

sealed interface DataError : RootError {
    enum class Network : DataError {
        REQUEST_TIEMOUT,
        TOO_MANY_REQUESTS,
        UNKNOW
        //...
    }

    enum class Cognito : DataError {
        UNKNOWN_ERROR,
        INVALID_EMAIL_OR_PASSWORD,
        SESSION_FETCH,
        SIGN_UP_FIELDS_NOT_VALID,
        WRONG_CONFIRMATION_CODE,
        SIGN_OUT_FAILED_USER_SIGNED_IN,
        SIGN_OUT_FAILED_USER_NOT_SIGNED_IN
    }
}