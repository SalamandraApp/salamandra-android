package com.android.salamandra.domain.error

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIEMOUT,
        TOO_MANY_REQUESTS,
        UNKNOW
        //...
    }
    enum class Cognito: DataError {
        INVALID_EMAIL_OR_PASSWORD,
        SESSION_FETCH
    }
}