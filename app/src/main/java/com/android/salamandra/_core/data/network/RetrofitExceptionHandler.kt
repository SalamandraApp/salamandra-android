package com.android.salamandra._core.data.network

import com.android.salamandra._core.domain.error.DataError
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class RetrofitExceptionHandler @Inject constructor() {
    fun handleException(exception: Exception): DataError.Network{
        return when (exception) {
            is HttpException -> handleHTTPException(exception)
            is ConnectException -> DataError.Network.NO_CONNECTION
            else -> DataError.Network.UNKNOWN
        }
    }
    private fun handleHTTPException(exception: HttpException): DataError.Network {
        return when (exception.code()){
            400 -> DataError.Network.BAD_REQUEST
            401 -> DataError.Network.UNAUTHORISED
            403 ->DataError.Network.FORBIDDEN
            404 -> DataError.Network.NOT_FOUND
            408-> DataError.Network.REQUEST_TIMEOUT
            429-> DataError.Network.TOO_MANY_REQUESTS
            500 ->DataError.Network.INTERNAL_SERVER_ERROR
            else -> DataError.Network.UNKNOWN
        }
    }
}