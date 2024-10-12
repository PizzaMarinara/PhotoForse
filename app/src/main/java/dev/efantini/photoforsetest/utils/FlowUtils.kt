package dev.efantini.photoforsetest.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T : Any> toResultFlow(call: suspend () -> Response<T>?): Flow<AsyncState<T>> =
    flow {
        emit(AsyncState.Loading())
        val c = call()
        c?.let {
            try {
                if (c.isSuccessful) {
                    c.body()?.let {
                        emit(AsyncState.Success(it))
                    }
                } else {
                    c.errorBody()?.let {
                        val error = it.string()
                        it.close()
                        emit(AsyncState.Error(Exception(error)))
                    }
                }
            } catch (e: Exception) {
                emit(AsyncState.Error(Exception(e.toString())))
            }
        } ?: emit(AsyncState.Error(Exception()))
    }.flowOn(Dispatchers.IO)
