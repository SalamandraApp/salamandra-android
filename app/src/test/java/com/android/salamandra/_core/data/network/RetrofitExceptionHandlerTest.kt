package com.android.salamandra._core.data.network

import com.android.salamandra._core.domain.error.DataError
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

class RetrofitExceptionHandlerTest {
    private val retrofitExceptionHandler = RetrofitExceptionHandler()

    @Test
    fun `test no connection`() {
        assert(retrofitExceptionHandler.handleException(ConnectException()) == DataError.Network.NO_CONNECTION)
    }

    @Test
    fun `test http error code 400`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(400, "".toResponseBody(null)))) == DataError.Network.BAD_REQUEST)
    }

    @Test
    fun `test http error code 401`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(401, "".toResponseBody(null)))) == DataError.Network.UNAUTHORISED)
    }

    @Test
    fun `test http error code 403`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(403, "".toResponseBody(null)))) == DataError.Network.FORBIDDEN)
    }

    @Test
    fun `test http error code 404`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(404, "".toResponseBody(null)))) == DataError.Network.NOT_FOUND)
    }

    @Test
    fun `test http error code 408`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(408, "".toResponseBody(null)))) == DataError.Network.REQUEST_TIMEOUT)

    }

    @Test
    fun `test http error code 429`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(429, "".toResponseBody(null)))) == DataError.Network.TOO_MANY_REQUESTS)
    }

    @Test
    fun `test http error code 500`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(500, "".toResponseBody(null)))) == DataError.Network.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `test http unknown code`() {
        assert(retrofitExceptionHandler.handleException(HttpException(Response.error<String>(800, "".toResponseBody(null)))) == DataError.Network.UNKNOWN)
    }
}