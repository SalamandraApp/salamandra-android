package com.android.salamandra.domain.error

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIEMOUT,
        TOO_MANY_REQUESTS,
        UNKNOW
        //...
    }
    enum class Cognito: DataError {
        SIGN_IN_AUTH
    }
}