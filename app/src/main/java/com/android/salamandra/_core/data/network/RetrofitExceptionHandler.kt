package com.android.salamandra._core.data.network

import com.android.salamandra._core.domain.error.DataError
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class RetrofitExceptionHandler @Inject constructor() {
    fun handleException(exception: Exception): DataError.Network{
        return when (exception) {
            is HttpException -> handleHTTPException(exception)
            is ConnectException -> handleNoConnectionException(exception)
            else -> DataError.Network.UNKNOWN
        }
    }
    private fun handleHTTPException(exception: HttpException): DataError.Network {
        return when (exception.code()){
            429-> DataError.Network.TOO_MANY_REQUESTS
            408-> DataError.Network.REQUEST_TIMEOUT
            else -> DataError.Network.UNKNOWN
        }
    }

    private fun handleNoConnectionException(exception: ConnectException): DataError.Network{
        return DataError.Network.NO_CONNECTION
    }
}