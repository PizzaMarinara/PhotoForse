package dev.efantini.photoforsetest.utils

sealed class AsyncState<out T : Any> {
    data class Loading(
        val isLoading: Boolean = true,
    ) : AsyncState<Nothing>()

    data class Success<out T : Any>(
        val data: T,
    ) : AsyncState<T>()

    data class Error(
        val exception: Exception,
        val errorCode: Int? = null,
    ) : AsyncState<Nothing>()
}
