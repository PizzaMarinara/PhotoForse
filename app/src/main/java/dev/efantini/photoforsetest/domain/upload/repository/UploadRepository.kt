package dev.efantini.photoforsetest.domain.upload.repository

import dev.efantini.photoforsetest.utils.AsyncState
import kotlinx.coroutines.flow.Flow

interface UploadRepository {
    fun uploadImage(byteArray: ByteArray): Flow<AsyncState<String>>
}
