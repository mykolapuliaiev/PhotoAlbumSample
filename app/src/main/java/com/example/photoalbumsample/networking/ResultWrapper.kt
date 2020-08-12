package com.example.photoalbumsample.networking

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val message: String? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()

    companion object {
        private val TAG = this::class.java.simpleName

        suspend fun <T> safeApiCall(
            dispatcher: CoroutineDispatcher,
            apiCall: suspend () -> T
        ): ResultWrapper<T> {
            return withContext(dispatcher) {
                try {
                    Success(apiCall.invoke())
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> NetworkError
                        is HttpException -> processHttpException(throwable)
                        else -> GenericError(null, null)
                    }
                }
            }
        }

        private fun <T> processHttpException(throwable: HttpException): ResultWrapper<T> {
            val code = throwable.code()
            val message = throwable.message()
            return GenericError(code, message)
        }
    }
}